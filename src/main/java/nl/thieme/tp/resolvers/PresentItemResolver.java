package nl.thieme.tp.resolvers;

import net.ess3.api.IItemDb;
import nl.thieme.tp.models.Present;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PresentItemResolver implements IItemDb.ItemResolver {

    private final List<Present> presents;
    private final List<String> names = new ArrayList<>();

    public PresentItemResolver(List<Present> presentList) {
        this.presents = presentList;
        for(Present present : presentList) {
            names.add(present.getName());
        }
    }

    @Override
    public ItemStack apply(String name) {
        return presents.get(0);
    }

    @Override
    public Collection<String> getNames() {
        return names;
    }

    @Override
    public String serialize(ItemStack stack) {
        return null;
    }
}
