package nl.thieme.tp.utils;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import nl.thieme.tp.Main;
import nl.thieme.tp.configs.MessageConfig;
import nl.thieme.tp.events.custom.PresentOpenEvent;
import nl.thieme.tp.events.custom.PresentWrapEvent;
import nl.thieme.tp.models.PresentNBT;
import nl.thieme.tp.models.TPermission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
        return NBTEditor.contains(is, presentNBTKey);
    }

    private static boolean isPresentItemStackWithNBT(ItemStack is) {
        return isPresentItemStack(is) && getPresentNBT(is) != null;
    }

    public static ItemStack setPresentMeta(ItemStack is, PresentNBT nbt) {
        String nbtData = presentNBTToString(nbt);
        return NBTEditor.set(is, nbtData, presentNBTKey);
    }

    public static PresentNBT getPresentNBT(ItemStack is) {
        String nbtData = NBTEditor.getString(is, presentNBTKey);
        if (nbtData.length() == 0) return null;
        try {
            return stringToPresentNBT(nbtData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void open(ItemStack is, Player p) {
        PresentOpenEvent.Pre poe = new PresentOpenEvent.Pre(is, p);
        Bukkit.getPluginManager().callEvent(poe);
        if (poe.isCancelled()) return;

        if (!isPresentItemStackWithNBT(is)) return;
        ItemStack present = getPresentNBT(is).getPresent().clone();
        addPresentToInventory(is, p, present);

        Bukkit.getPluginManager().callEvent(new PresentOpenEvent.Post(is, present, p));
    }

    private static void addPresentToInventory(ItemStack is, Player p, ItemStack present) {
        if (p.getInventory().getItemInMainHand().equals(is)) {
            p.getInventory().setItemInMainHand(present);
        } else {
            int slot = p.getInventory().first(is);
            if (slot != -1) {
                p.getInventory().clear(slot); // remove by slot
                p.getInventory().addItem(present);
            } else Main.LOGGER.warning("How did you even manage to get this error?!");
        }
    }

    public static void wrap(ItemStack present, ItemStack toBeWrapped, Player p) {
        PresentWrapEvent.Pre pwe = new PresentWrapEvent.Pre(present, toBeWrapped, p);
        Bukkit.getPluginManager().callEvent(pwe);
        if (pwe.isCancelled()) return;

        if (!TPermission.hasPermission(p, TPermission.NP_WRAP)) return;

        if (!p.getInventory().contains(toBeWrapped)) return; // item removed from inventory


        if (!removeItem(p, toBeWrapped)) { // fails to remove last clicked slot item
            p.getInventory().remove(toBeWrapped);
            MsgUtil.debugInfo("Couldnt remove InvStack");
        }
        p.closeInventory();

        if (!isPresentItemStackWithNBT(present)) return; // if hotbar changed
        PresentNBT presentNBT = getPresentNBT(present);
        presentNBT.setPresent(toBeWrapped);

        if (presentNBT.closed_head != null)
            present.setItemMeta(HeadUtil.setHeadUrl(presentNBT.closed_head, present.getItemMeta()));
        String loreAdd = MessageConfig.MessageKey.SIGN_FROM.get();

        if (loreAdd.length() > 0)
            present.setItemMeta(HeadUtil.addLore(present.getItemMeta(), loreAdd.replaceAll(MsgUtil.fromKey, p.getName())));

        present.setItemMeta(setPresentMeta(present, presentNBT).getItemMeta());

        Bukkit.getPluginManager().callEvent(new PresentWrapEvent.Post(present, toBeWrapped, p));
    }

    private static boolean removeItem(Player p, ItemStack is) {
        if (!InvUtil.lastClickedSlot.containsKey(p.getUniqueId())) return false;
        int cachedSlot = InvUtil.lastClickedSlot.get(p.getUniqueId());
        InvUtil.lastClickedSlot.remove(p.getUniqueId());
        int reversed = InvUtil.reverseSlotThiemeWay(cachedSlot);
        MsgUtil.debugInfo("cachedSlot: " + cachedSlot);
        MsgUtil.debugInfo("reversedSlot: " + reversed);
        ItemStack invStack = p.getInventory().getItem(reversed);
        if (invStack != null && invStack.isSimilar(is)) {
            p.getInventory().setItem(reversed, null);
            return true;
        }
        return false;
    }


}
