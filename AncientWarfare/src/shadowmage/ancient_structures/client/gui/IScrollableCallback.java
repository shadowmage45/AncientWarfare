package shadowmage.ancient_structures.client.gui;

/**
 * GUI's / composite elements should implement this and register with
 * a scroll-bar to be informed when the scrolled view changes
 * @author Shadowmage
 *
 */
public interface IScrollableCallback
{

public void onScrolled(int newTop);

}
