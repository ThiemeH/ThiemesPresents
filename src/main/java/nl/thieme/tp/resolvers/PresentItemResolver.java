package nl.thieme.tp.resolvers;

import net.ess3.api.IItemDb;
import nl.thieme.tp.models.Present;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class PresentItemResolver implements IItemDb.ItemResolver {

    private final HashMap<String, Present> namePresentMap = new HashMap<>();

    public PresentItemResolver(List<Present> presentList) {
        for (Present present : presentList) {
            namePresentMap.put(present.getName().toLowerCase(), present);
        }
    }

    @Override
    public ItemStack apply(String name) {
        name = name.toLowerCase();
        if (namePresentMap.containsKey(name)) {
            return namePresentMap.get(name);
        }
        return null;
    }

    @Override
    public Collection<String> getNames() {
        return namePresentMap.keySet();
    }

}
