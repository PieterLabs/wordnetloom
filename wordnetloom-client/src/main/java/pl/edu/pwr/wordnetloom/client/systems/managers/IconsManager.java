package pl.edu.pwr.wordnetloom.client.systems.managers;

import javax.swing.ImageIcon;


public class IconsManager {

    private static final ImageIcon newImage = new ImageIcon(IconsManager.class.getClassLoader().getResource("icons/new.gif"));
    private static final ImageIcon newImage2 = new ImageIcon(IconsManager.class.getClassLoader().getResource("icons/new2.gif"));
    private static final ImageIcon upLUImage = new ImageIcon(IconsManager.class.getClassLoader().getResource("icons/upLU.gif"));
    private static final ImageIcon downLUImage = new ImageIcon(IconsManager.class.getClassLoader().getResource("icons/downLU.gif"));
    private static final ImageIcon relationsImage = new ImageIcon(IconsManager.class.getClassLoader().getResource("icons/relations.gif"));
    private static final ImageIcon switchImage = new ImageIcon(IconsManager.class.getClassLoader().getResource("icons/switchToUnits.gif"));
    private static final ImageIcon addImage = new ImageIcon(IconsManager.class.getClassLoader().getResource("icons/add.gif"));
    private static final ImageIcon deleteImage = new ImageIcon(IconsManager.class.getClassLoader().getResource("icons/delete.gif"));
    private static final ImageIcon duplicateImage = new ImageIcon(IconsManager.class.getClassLoader().getResource("icons/duplicateSynset.gif"));
    private static final ImageIcon insertBetweenImage = new ImageIcon(IconsManager.class.getClassLoader().getResource("icons/insertBetween.gif"));
    private static final ImageIcon toNew = new ImageIcon(IconsManager.class.getClassLoader().getResource("icons/toNewSmall.gif"));
    private static final ImageIcon edit = new ImageIcon(IconsManager.class.getClassLoader().getResource("icons/edit.gif"));
    private static final ImageIcon busy = new ImageIcon(IconsManager.class.getClassLoader().getResource("icons/busy.gif"));
    private static final ImageIcon right = new ImageIcon(IconsManager.class.getClassLoader().getResource("icons/right.png"));
    private static final ImageIcon user = new ImageIcon(IconsManager.class.getClassLoader().getResource("icons/user.png"));
    

    private IconsManager() {
    }

    /**
     * ikona do gory
     *
     * @return ikona
     */
    public static ImageIcon getUp() {
        return upLUImage;
    }

    /**
     * ikona w dol
     *
     * @return ikona
     */
    public static ImageIcon getDown() {
        return downLUImage;
    }

    /**
     * ikona relacji
     *
     * @return ikona
     */
    public static ImageIcon getRelations() {
        return relationsImage;
    }

    /**
     * ikona dodaj
     *
     * @return ikona
     */
    public static ImageIcon getAdd() {
        return addImage;
    }

    /**
     * ikona usun
     *
     * @return ikona
     */
    public static ImageIcon getDelete() {
        return deleteImage;
    }

    public static ImageIcon getRight() {
        return right;
    }

    /**
     * ikona utworzenie nowego synsetu
     *
     * @return ikona
     */
    public static ImageIcon getToNew() {
        return toNew;
    }

    /**
     * ikona nowy
     *
     * @return ikona
     */
    public static ImageIcon getNew() {
        return newImage;
    }

    /**
     * ikona nowy
     *
     * @return ikona
     */
    public static ImageIcon getNew2() {
        return newImage2;
    }

    /**
     * ikona przelaczenia
     *
     * @return ikona
     */
    public static ImageIcon getSwitch() {
        return switchImage;
    }

    /**
     * ikona powiel
     *
     * @return ikona
     */
    public static ImageIcon getDuplicate() {
        return duplicateImage;
    }

    /**
     * ikona wstawiania relacji w srodek
     *
     * @return ikona
     */
    public static ImageIcon getInsertBetween() {
        return insertBetweenImage;
    }

    /**
     * icon small busy circle
     *
     * @return ikona
     */
    public static ImageIcon getSmallBusy() {
        return busy;
    }

    public static ImageIcon getEdit() {
        return edit;
    }

    public static ImageIcon getUser() {
        return user;
    }
}