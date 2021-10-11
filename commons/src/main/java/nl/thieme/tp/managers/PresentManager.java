package nl.thieme.tp.managers;

import nl.thieme.tp.ThiemesPresents;
import nl.thieme.tp.essentials.EssentialResolverLoader;
import nl.thieme.tp.models.Present;

import java.util.ArrayList;
import java.util.List;

public class PresentManager {

    private List<Present> presents;

    public PresentManager() {
        presents = new ArrayList<>();
    }

    public List<Present> getPresents() {
        return presents;
    }

    public void addPresent(Present present) {
        presents.add(present);
    }
    public void addPresents(List<Present> presentList) {
        presents.addAll(presentList);
    }
    public void clearPresents() {
        presents.clear();
    }

    public boolean removePresent(Present present) {
        return presents.remove(present);
    }

    public void reloadEssentialItems() {
        if(ThiemesPresents.INSTANCE.hasEssentials) {
            EssentialResolverLoader.removeItems(ThiemesPresents.INSTANCE);
            EssentialResolverLoader.addItemsToEssentials(ThiemesPresents.INSTANCE);
        }
    }

}
