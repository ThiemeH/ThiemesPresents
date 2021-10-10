package nl.thieme.tp.utils;

import nl.thieme.tp.Main;
import nl.thieme.tp.configs.MessageConfig;
import nl.thieme.tp.models.PresentNBT;
import nl.thieme.tp.models.TPermission;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;


public class PresentUtil {

    public static NamespacedKey presentNBTKey = new NamespacedKey(Main.INSTANCE, "presentnbt");

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
        return NBTEditor.set(is,nbtData,  presentNBTKey);
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
        if (!isPresentItemStackWithNBT(is)) return;
        ItemStack present = getPresentNBT(is).getPresent().clone();
        addPresentToInventory(is, p, present);
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

    public static void wrap(ItemStack is, ItemStack present, Player p) {
        if(!TPermission.hasPermission(p, TPermission.NP_WRAP)) return;

        if (!p.getInventory().contains(present)) return; // item removed from inventory
        p.getInventory().remove(present);
        p.closeInventory();

        if (!isPresentItemStackWithNBT(is)) return; // if hotbar changed
        PresentNBT presentNBT = getPresentNBT(is);
        presentNBT.setPresent(present);

        if (presentNBT.closed_head != null)
            is.setItemMeta(HeadUtil.setHeadUrl(presentNBT.closed_head, is.getItemMeta()));
        String loreAdd = MessageConfig.MessageKey.SIGN_FROM.get();

        if (loreAdd.length() > 0)
            is.setItemMeta(HeadUtil.addLore(is.getItemMeta(), loreAdd.replaceAll(MsgUtil.fromKey, p.getName())));

        setPresentMeta(is, presentNBT);
    }

}
