package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;
import java.util.Locale;

import javax.swing.*;

import log.Logger;

public class MainApplicationFrame extends JFrame
{
    Locale currentLocale = new Locale("ru", "RU");
    ResourceBundle messages = ResourceBundle.getBundle("resources",  currentLocale);
    private final JDesktopPane desktopPane = new JDesktopPane();

    public MainApplicationFrame() {
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width  - inset*2,
                screenSize.height - inset*2);

        setContentPane(desktopPane);

        LogWindow logWindow = createLogWindow();
        addWindow(logWindow, 150, 350);

        GameWindow gameWindow = new GameWindow();
        addWindow(gameWindow, 400, 400);

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    protected LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        Logger.debug(messages.getString("ProtocolIsWorking"));
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame, int width, int height)
    {
        desktopPane.add(frame);
        frame.setSize(width, height);
        frame.setVisible(true);
    }

    private JMenuBar generateMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(createFileMenu());

        menuBar.add(createLookAndFeelMenu());

        menuBar.add(createTestMenu());

        return menuBar;
    }

    private JMenu createFileMenu() {

        JMenu menu = new JMenu(messages.getString("Menu"));
        menu.setMnemonic(KeyEvent.VK_D);

        menu.add(createMenuItem(messages.getString("NewGameWindow"), KeyEvent.VK_N, KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK), (event) -> {
            GameWindow window = new GameWindow();
            addWindow(window, 400, 400);
        }));


        menu.add(createMenuItem(messages.getString("LogsWindow"), KeyEvent.VK_L, KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK), (event) -> {
            LogWindow window = new LogWindow(Logger.getDefaultLogSource());
            addWindow(window, 150, 350);
        }));

        menu.add(exit());

        return menu;
    }

    private JMenuItem createMenuItem(String text, int mnemonic, KeyStroke accelerator, ActionListener action){
        JMenuItem item = new JMenuItem(text);
        item.setMnemonic(mnemonic);
        item.setAccelerator(accelerator);
        item.addActionListener(action);

        return item;

    }


    private JMenu createLookAndFeelMenu() {
        JMenu lookAndFeelMenu = new JMenu(messages.getString("DisplayMode"));
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(messages.getString("ModeControl"));

        JMenuItem systemLookAndFeel = new JMenuItem(messages.getString("SystemDiagram"), KeyEvent.VK_S);
        systemLookAndFeel.addActionListener((event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        });
        lookAndFeelMenu.add(systemLookAndFeel);

        JMenuItem crossplatformLookAndFeel = new JMenuItem(messages.getString("UniversalScheme"), KeyEvent.VK_U);
        crossplatformLookAndFeel.addActionListener((event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        });
        lookAndFeelMenu.add(crossplatformLookAndFeel);

        return lookAndFeelMenu;
    }

    private JMenuItem exit(){
        JMenuItem exitMenuItem = new JMenuItem(messages.getString("Exit"));
        exitMenuItem.setMnemonic(KeyEvent.VK_Q);
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
        exitMenuItem.addActionListener((event) -> {
            exitApplication();
        });
        return exitMenuItem;
    }

    private JMenu createTestMenu() {
        JMenu testMenu = new JMenu(messages.getString("Tests"));
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(messages.getString("TestsCommands"));

        JMenuItem addLogMessageItem = new JMenuItem(messages.getString("MessageLog"), KeyEvent.VK_S);
        addLogMessageItem.addActionListener((event) -> {
            Logger.debug(messages.getString("NewString"));
        });
        testMenu.add(addLogMessageItem);

        return testMenu;
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }

    private void exitApplication() {
        UIManager.put("OptionPane.yesButtonText", messages.getString("Yes"));
        UIManager.put("OptionPane.noButtonText", messages.getString("No"));

        int confirmation = JOptionPane.showConfirmDialog(this, messages.getString("ConfirmationExitQuestion"), messages.getString("ConfirmationExit"), JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            this.dispose();
        }
    }
}