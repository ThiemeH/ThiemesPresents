package nl.thieme.tp.utils;

import nl.thieme.tp.Main;
import nl.thieme.tp.models.PresentNBT;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
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
        return !is.hasItemMeta() ? false : is.getItemMeta().getPersistentDataContainer().has(presentNBTKey, PersistentDataType.STRING);
    }

    public static ItemMeta setPresentMeta(ItemMeta im, PresentNBT nbt) {
        String nbtData = presentNBTToString(nbt);
        im.getPersistentDataContainer().set(presentNBTKey, PersistentDataType.STRING, nbtData);
        return im;
    }

    public static PresentNBT getPresentNBT(ItemStack is) {
        String nbtData = is.getItemMeta().getPersistentDataContainer().get(presentNBTKey, PersistentDataType.STRING);
        if(nbtData.length() == 0) return null;
        try {
            return stringToPresentNBT(nbtData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void open(ItemStack is, Player p) {
        PresentNBT presentNBT = getPresentNBT(is);
        p.getInventory().addItem(presentNBT.getPresent());
        presentNBT.setPresent(null);
        is.setAmount(0);
    }

    public static void wrap(ItemStack is, ItemStack present, Player p) {
        PresentNBT presentNBT = getPresentNBT(is);
        presentNBT.setPresent(present);
        if(presentNBT.closed_head != null) is.setItemMeta(HeadUtil.setHeadUrl(presentNBT.closed_head, is.getItemMeta()));
        is.setItemMeta(setPresentMeta(is.getItemMeta(), presentNBT));
    }
}
