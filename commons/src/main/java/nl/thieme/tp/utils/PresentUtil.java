package nl.thieme.tp.utils;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import nl.thieme.tp.configs.MainConfig;
import nl.thieme.tp.configs.MessageConfig;
import nl.thieme.tp.events.custom.PresentOpenEvent;
import nl.thieme.tp.events.custom.PresentWrapEvent;
import nl.thieme.tp.models.FullSound;
import nl.thieme.tp.models.PresentNBT;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;

public class PresentUtil {

    public static final String presentNBTKey = "presentnbt";

    public static String presentNBTToString(PresentNBT nbt) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream dataOutput = new ObjectOutputStream(outputStream);
            dataOutput.writeObject(nbt);
            dataOutput.flush();
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static PresentNBT stringToPresentNBT(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            ObjectInputStream dataInput = new ObjectInputStream(inputStream);
            PresentNBT item = (PresentNBT) dataInput.readObject();
            dataInput.close();
            return item;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

    public static boolean isPresentItemStack(ItemStack is) {
        return is != null && NBTEditor.contains(is, presentNBTKey);
    }

    private static boolean isPresentItemStackWithNBT(ItemStack is) {
        return isPresentItemStack(is) && getPresentNBT(is) != null;
    }

    public static ItemMeta setPresentMeta(ItemStack is, PresentNBT nbt) {
        String nbtData = presentNBTToString(nbt);
        return NBTEditor.set(is, nbtData, presentNBTKey).getItemMeta();
    }

    public static PresentNBT getPresentNBT(ItemStack is) {
        String nbtData = NBTEditor.getString(is, presentNBTKey);
        if (nbtData == null || nbtData.length() == 0) return null;
        try {
            return stringToPresentNBT(nbtData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void open(ItemStack is, Player p) {
        // Custom event call
        PresentOpenEvent.Pre poe = new PresentOpenEvent.Pre(is, p);
        Bukkit.getPluginManager().callEvent(poe);
        if (poe.isCancelled()) return;

        if (!isPresentItemStackWithNBT(is)) return;
        PresentNBT presentNBT = getPresentNBT(is);
        if (presentNBT == null) return;

        ItemStack present = presentNBT.getPresent().clone();
        if (!tryAddPresentToInventory(is, p, present)) {
            MsgUtil.sendMessage(p, MessageConfig.MessageKey.INV_FULL);
            return;
        }
        FullSound fs = MainConfig.ConfigKey.OPEN_SOUND.getFullSound();
        if (fs != null) p.playSound(p.getLocation(), fs.getXSound().parseSound(), fs.getVolume(), fs.getPitch());
        Bukkit.getPluginManager().callEvent(new PresentOpenEvent.Post(is, present, p));
    }

    private static boolean tryAddPresentToInventory(ItemStack is, Player p, ItemStack present) {
        if (p.getInventory().getItemInMainHand().equals(is)) {
            p.getInventory().setItemInMainHand(present);
            return true;
        }

        int slot = p.getInventory().first(is);
        if (slot != -1) {
            p.getInventory().clear(slot); // remove by slot
            p.getInventory().addItem(present);
            return true;
        }

        return false;
    }

    public static void wrap(ItemStack present, ItemStack toBeWrapped, Player p) {
        if (!p.getInventory().contains(toBeWrapped)) return; // item removed from inventory

        // Custom event
        PresentWrapEvent.Pre pwe = new PresentWrapEvent.Pre(present, toBeWrapped, p);
        Bukkit.getPluginManager().callEvent(pwe);
        if (pwe.isCancelled()) return;

        if (!removeItem(p, toBeWrapped)) { // fails to remove last clicked slot item
            p.getInventory().remove(toBeWrapped);
            MsgUtil.debugInfo("Couldnt remove InvStack");
        }
        p.closeInventory();

        if (!isPresentItemStackWithNBT(present)) return; // if hotbar changed
        PresentNBT presentNBT = getPresentNBT(present);
        presentNBT.setPresent(toBeWrapped);

        if (presentNBT.closedHead != null)
            present.setItemMeta(HeadUtil.setHeadUrl(presentNBT.closedHead, present.getItemMeta()));

        String loreBase = MainConfig.ConfigKey.CAN_SIGN.getBoolean() ? MessageConfig.MessageKey.LORE_WRAPPED.get() : MessageConfig.MessageKey.LORE_SIGNED.get();
        if (loreBase.length() > 0) present.setItemMeta(HeadUtil.setLore(present.getItemMeta(), loreBase));

        String loreAdd = MessageConfig.MessageKey.SIGN_FROM.get();
        if (loreAdd.length() > 0) {
            presentNBT.fromPlayerName = p.getName();
            present.setItemMeta(HeadUtil.addLore(present.getItemMeta(), getFromFormat(p.getName())));
        }

        present.setItemMeta(setPresentMeta(present, presentNBT));

        FullSound fs = MainConfig.ConfigKey.WRAP_SOUND.getFullSound();
        if (fs != null) p.playSound(p.getLocation(), fs.getXSound().parseSound(), fs.getVolume(), fs.getPitch());

        Bukkit.getPluginManager().callEvent(new PresentWrapEvent.Post(present, toBeWrapped, p, presentNBT));
    }

    private static boolean removeItem(Player p, ItemStack is) {
        if (!InvUtil.lastClickedSlot.containsKey(p.getUniqueId())) return false;
        int cachedSlot = InvUtil.lastClickedSlot.get(p.getUniqueId());
        InvUtil.lastClickedSlot.remove(p.getUniqueId());
        int reversed = InvUtil.reverseSlotThiemeWay(cachedSlot);
        ItemStack invStack = p.getInventory().getItem(reversed);
        if (invStack != null && invStack.isSimilar(is)) {
            p.getInventory().setItem(reversed, null);
            return true;
        }
        return false;
    }

    public static boolean addSignedNBT(ItemStack is, String msgRaw) {
        PresentNBT presentNBT = PresentUtil.getPresentNBT(is);
        if (presentNBT == null) return false;
        presentNBT.isSigned = true;

        String loreBase = MessageConfig.MessageKey.LORE_SIGNED.get();
        if (loreBase.length() > 0) is.setItemMeta(HeadUtil.setLore(is.getItemMeta(), loreBase));

        if (presentNBT.fromPlayerName.length() > 0)
            is.setItemMeta(HeadUtil.addLore(is.getItemMeta(), getFromFormat(presentNBT.fromPlayerName)));

        String msg = MsgUtil.replaceColors(getToFormat(msgRaw));
        is.setItemMeta(HeadUtil.addLore(is.getItemMeta(), MsgUtil.replaceColors(msg)));
        is.setItemMeta(setPresentMeta(is, presentNBT));
        return true;
    }

    public static String getToFormat(String s) {
        return MessageConfig.MessageKey.SIGN_TO.get().replaceAll(MsgUtil.toKey, s);
    }

    public static String getFromFormat(String s) {
        return MessageConfig.MessageKey.SIGN_FROM.get().replaceAll(MsgUtil.fromKey, s);
    }
}
