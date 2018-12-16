
package button;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * @author Michael
 */
public class Button extends MouseAdapter implements MouseMotionListener {
    protected final JPanel panel;
    protected Rectangle rect;
    protected Font font;
    protected String text;

    protected Color color, fontColor;
    private ButtonPlan plan;

    private boolean visible, enabled,
            switchable, selected, //like radio button
            checkable, on; //like toggle button

    private boolean down;
    private ActionListener[] actionListeners;

    private Point click;

    public void doClick() {
        mousePressed(null);
        mouseReleased(null);
    }

    public void doClick(boolean press) {
        if (press)
            mousePressed(null);
        else
            mouseReleased(null);
    }

    private void addListeners() {
        this.panel.addMouseListener(this);
        this.panel.addMouseMotionListener(this);
    }

    public void kill() {
        panel.removeMouseListener(this);
        panel.removeMouseMotionListener(this);
    }

    public static class ButtonPlan implements Serializable {
        int value = 50;

        void drawButton(Graphics g, boolean down, Rectangle rect, Color color) {
        }

        void setValue(int value) {
            this.value = value;
        }
    }

    public static final ButtonPlan GRADIENT = new ButtonPlan() {
        @Override
        public void drawButton(Graphics g, boolean down, Rectangle rect, Color color) {
            g.setColor(color);
            int red, green, blue;
            int mid = rect.y + rect.height / 2;

            if (!down) {
                for (int i = 0; i <= rect.height / 2; i++) {
                    red = (int) (color.getRed() + i * value / (rect.height / 2));
                    green = (int) (color.getGreen() + i * value / (rect.height / 2));
                    blue = (int) (color.getBlue() + i * value / (rect.height / 2));
                    if (red > 255)
                        red = 255;
                    if (green > 255)
                        green = 255;
                    if (blue > 255)
                        blue = 255;
                    g.setColor(new Color(red, green, blue));
                    g.drawLine(rect.x, mid - i, rect.x + rect.width - 1, mid - i);    //mid to top
                }
                for (int i = 0; i >= -rect.height / 2; i--) {
                    red = (int) (color.getRed() + i * value / (rect.height / 2));
                    green = (int) (color.getGreen() + i * value / (rect.height / 2));
                    blue = (int) (color.getBlue() + i * value / (rect.height / 2));
                    if (red < 0)
                        red = 0;
                    if (green < 0)
                        green = 0;
                    if (blue < 0)
                        blue = 0;
                    g.setColor(new Color(red, green, blue));
                    g.drawLine(rect.x, mid - i, rect.x + rect.width - 1, mid - i);    //mid to bottom
                }
            }
            else {
                for (int i = 0; i <= rect.height / 2; i++) {
                    red = (int) (color.getRed() + i * value / (rect.height / 2));
                    green = (int) (color.getGreen() + i * value / (rect.height / 2));
                    blue = (int) (color.getBlue() + i * value / (rect.height / 2));
                    if (red > 255)
                        red = 255;
                    if (green > 255)
                        green = 255;
                    if (blue > 255)
                        blue = 255;
                    g.setColor(new Color(red, green, blue));
                    g.drawLine(rect.x, mid + i, rect.x + rect.width - 1, mid + i);    //mid to bottom
                }
                for (int i = 0; i >= -rect.height / 2; i--) {
                    red = (int) (color.getRed() + i * value / (rect.height / 2));
                    green = (int) (color.getGreen() + i * value / (rect.height / 2));
                    blue = (int) (color.getBlue() + i * value / (rect.height / 2));
                    if (red < 0)
                        red = 0;
                    if (green < 0)
                        green = 0;
                    if (blue < 0)
                        blue = 0;
                    g.setColor(new Color(red, green, blue));
                    g.drawLine(rect.x, mid + i, rect.x + rect.width - 1, mid + i);    //mid to top
                }
            }
        }
    };
    public static final ButtonPlan CLASSIC = new ButtonPlan() {
        @Override
        public void drawButton(Graphics g, boolean down, Rectangle rect, Color color) {
            g.setColor(color);
            g.fill3DRect(rect.x, rect.y, rect.width, rect.height, !down);
        }
    };
    public static final ButtonPlan STANDARD = new ButtonPlan() {
        @Override
        public void drawButton(Graphics g, boolean down, Rectangle rect, Color color) {
            g.setColor(color);
            if (down)
                g.setColor(color.darker());
            g.fillRect(rect.x, rect.y, rect.width, rect.height);
        }
    };

    public Button(JPanel panel, Rectangle rect, String text, ButtonPlan plan) { //no color, uses panel's font
        this.panel = panel;
        this.rect = rect;
        this.text = text;
        this.plan = plan;

        visible = true;
        enabled = true;
        actionListeners = new ActionListener[0];
        font = panel.getFont();
        fontColor = Color.BLACK;
        addListeners();
    }

    public Button(JPanel panel, Rectangle rect, String text, Color color, ButtonPlan plan) {//uses panel's font
        this.panel = panel;
        this.rect = rect;
        this.text = text;
        this.color = color;
        this.plan = plan;

        visible = true;
        enabled = true;
        actionListeners = new ActionListener[0];
        font = panel.getFont();
        setFontColorForLuminance();

        addListeners();
    }

    public Button(JPanel panel, Rectangle rect, String text, Font font, ButtonPlan plan) {//no color
        this.panel = panel;
        this.rect = rect;
        this.text = text;
        this.font = font;
        this.plan = plan;

        visible = true;
        enabled = true;
        actionListeners = new ActionListener[0];
        fontColor = Color.BLACK;

        addListeners();
    }

    public Button(JPanel panel, Rectangle rect, String text, Font font, Color color, ButtonPlan plan) {
        this.panel = panel;
        this.rect = rect;
        this.text = text;
        this.font = font;
        this.color = color;
        this.plan = plan;

        visible = true;
        enabled = true;
        actionListeners = new ActionListener[0];
        fontColor = Color.WHITE;

        addListeners();
    }

    public JPanel getPanel() {
        return panel;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isSelected() {
        return selected;
    }

    public boolean isOn() {
        return on;
    }

    public void draw(Graphics g) {
        if (!visible)
            return;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (plan != null)
            plan.drawButton(g, down, rect, color);

        if (icon != null) {
            g.setColor(fontColor);
            icon.draw(g, rect.x, rect.y, rect.width, rect.height);
        }
        if (text!=null && !text.isEmpty() && fontScale > 0) {
            float fontSize = 20.0f;
            font = font.deriveFont(fontSize);
            int width = g.getFontMetrics(font).stringWidth(text); //get the width of the text at 20pt
            int height = g.getFontMetrics(font).getAscent(); //get the width of the text at 20pt

            float sizeX = (float) ((rect.width * fontScale / width) * fontSize); //multiply it by the scalar ratio between rectangle and text width
            float sizeY = (float) ((rect.height * fontScale / height) * fontSize); //multiply it by the scalar ratio between rectangle and text width
            font = font.deriveFont(Math.min(sizeX, sizeY));
        }
        //TEXT
        g.setColor(fontColor);
        Font temp = g.getFont();
        if (font != null)
            g.setFont(font);
        drawCenteredString(g, text, rect);
        g.setFont(temp);
    }

    private double fontScale;

    public void setFontScale(double fontScale) {
        this.fontScale = fontScale;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setPlan(ButtonPlan plan) {
        this.plan = plan;
    }

    private void setDown(boolean down) {
        this.down = down;
        panel.repaint();
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    public void setRect(int x, int y, int width, int height) {
        this.rect = new Rectangle(x, y, width, height);
    }

    public void setX(int x) {
        setRect(x, rect.y, rect.width, rect.height);
    }

    public void setY(int y) {
        setRect(rect.x, y, rect.width, rect.height);
    }

    public void setWidth(int width) {
        setRect(rect.x, rect.y, width, rect.height);
    }

    public void setHeight(int height) {
        setRect(rect.x, rect.y, rect.width, height);
    }

    public void setVisible(boolean v) {
        visible = v;
    }

    public boolean setEnabled(boolean e) {
        boolean toReturn = enabled;
        enabled = e;
        return toReturn;
    }

    public void setSelected(boolean s) {
        if (!switchable)
            return;
        selected = s;
        setDown(s);
    }

    public void addActionListener(ActionListener al) {
        ActionListener[] temp = new ActionListener[actionListeners.length + 1];
        for (int i = 0; i < actionListeners.length; i++) {
            ActionListener actionListener = actionListeners[i];
            if (actionListener.equals(al))
                return;
            temp[i] = actionListener;
        }
        temp[temp.length - 1] = al;
        actionListeners = temp;
    }

    public void setColor(Color c) {
        color = c;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setFontStyle(int style) {
        this.font = new Font(font.getName(), style, font.getSize());
    }

    public void setCheckable(boolean checkable) {
        this.checkable = checkable;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    public final void setFontColorForLuminance() {
        if (color == null)
            return;
        int r = color.getRed(), g = color.getGreen(), b = color.getBlue();
        int luminance = (int) Math.sqrt(0.299 * r * r + 0.587 * g * g + 0.114 * b * b);

        if (luminance < 127)
            fontColor = Color.WHITE;
        else
            fontColor = Color.BLACK;
    }

    public final void setFontColorForLuminanceOf(Color color) {
        int r = color.getRed(), g = color.getGreen(), b = color.getBlue();
        int luminance = (int) Math.sqrt(0.299 * r * r + 0.587 * g * g + 0.114 * b * b);

        if (luminance < 127)
            fontColor = Color.WHITE;
        else
            fontColor = Color.BLACK;
    }

    @Override
    public void mouseClicked(MouseEvent me) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
        if (me != null) {
            if (!SwingUtilities.isLeftMouseButton(me) || !visible || selected || !enabled)
                return;
            click = me.getPoint();
        }
        if (me == null || rect.contains(click))
            setDown(true);
        else
            click = null;
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        if (me != null)
            if (!SwingUtilities.isLeftMouseButton(me) || !visible || click == null || selected || !enabled
                    || !rect.contains(click)
                    || !rect.contains(me.getPoint())) {
                click = null;
                return;
            }

        if (checkable)
            on = !on;

        String cmd = "on";
        if (checkable && !on)
            cmd = "off";

        if (actionListeners != null)
            for (ActionListener actionListener : actionListeners)
                actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, cmd)); //perform all actions
        click = null;
        if (!switchable && !on)
            setDown(false);
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        if (!SwingUtilities.isLeftMouseButton(me) || !visible || click == null || selected || !enabled
                || (checkable && on))
            return;
        setDown(rect.contains(click) && rect.contains(me.getPoint()));
    }

    @Override
    public void mouseMoved(MouseEvent me) {
    }

    public static Button[] groupButtons(Button[] buttons) {
        for (Button button : buttons) {
            button.switchable = true;
            button.addActionListener((ActionEvent ae) -> {
                for (Button buttonToSwitch : buttons)
                    buttonToSwitch.setSelected(false);
                button.setSelected(true);
            });
        }
        return buttons;
    }

    public static List<Button> groupButtons(List<Button> buttons) {
        for (Button button : buttons) {
            button.switchable = true;
            button.addActionListener((ActionEvent ae) -> {
                for (Button buttonToSwitch : buttons)
                    buttonToSwitch.setSelected(false);
                button.setSelected(true);
            });
        }
        return buttons;
    }

    private static void drawCenteredString(Graphics g, String text, Rectangle rect) {
        if (text == null)
            return;
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.drawString(text, x, y);
    }

    private Icon icon;

    public static interface Icon {
        void draw(Graphics g, int x, int y, int width, int height);
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

}
