// Widgets
// Shows UI Components
import pulpcore.animation.Easing;
import pulpcore.image.CoreFont;
import pulpcore.image.CoreImage;
import pulpcore.Input;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.Button;
import pulpcore.sprite.FilledSprite;
import pulpcore.sprite.Group;
import pulpcore.sprite.ImageSprite;
import pulpcore.sprite.Label;
import pulpcore.sprite.Slider;
import pulpcore.sprite.Sprite;
import pulpcore.sprite.StretchableSprite;
import pulpcore.sprite.TextField;
import pulpcore.Stage;
import static pulpcore.image.Colors.*;

public class Widgets extends Scene2D {
    
    Label answer;
    TextField textField;
    TextField passwordField;
    Button okButton;
    Button checkbox;
    Group form;
    
    @Override
    public void load() {
        CoreFont font = CoreFont.getSystemFont().tint(WHITE);
        
        // Create the form fields
        Label label = new Label(font, "Name: ", 0, 0);
        label.setAnchor(Sprite.EAST);
        
        textField = new TextField("Suzy", 5, 0, 150, font.getHeight());
        textField.setAnchor(Sprite.WEST);
        textField.setFocus(true);
        
        Label label2 = new Label(font, "Secret Password: ", 0, 40);
        label2.setAnchor(Sprite.EAST);
        
        passwordField = new TextField(5, 40, 150, font.getHeight());
        passwordField.setPasswordMode(true);
        passwordField.setAnchor(Sprite.WEST);
        
        Slider slider = new Slider("slider.png", "slider-thumb.png", 0, 80);
        slider.setAnchor(Sprite.WEST);
        Label label3 = new Label(font, "Value: %d ", 0, 80);
        label3.setFormatArg(slider.value);
        label3.setAnchor(Sprite.EAST);
        
        CoreImage checkboxImage = CoreImage.load("checkbox.png");
        checkbox = Button.createLabeledToggleButton(checkboxImage.split(3,2), font,
            "I'm feeling slanted", 0, 120, 30, 12, Sprite.WEST, false);
        checkbox.setCursor(Input.CURSOR_DEFAULT);
        checkbox.setPixelLevelChecks(false);
        checkbox.setAnchor(Sprite.WEST);
        
        CoreImage buttonImage = CoreImage.load("button.png");
        okButton = new Button(buttonImage.split(3), 0, 160);
        okButton.setAnchor(Sprite.NORTH);
        okButton.setKeyBinding(Input.KEY_ENTER);
        
        answer = new Label(font, "", 0, 235);
        answer.setAnchor(Sprite.CENTER);
        
        // Add the form fields to a group
        form = new Group(Stage.getWidth() / 2, Stage.getHeight() / 2);
        form.setAnchor(Sprite.CENTER);
        form.add(new StretchableSprite("border.9.png", -225, -50, 480, 320));
        form.add(label);
        form.add(createTextFieldBackground(textField));
        form.add(textField);
        form.add(label2);
        form.add(createTextFieldBackground(passwordField));
        form.add(passwordField);
        form.add(label3);
        form.add(slider);
        form.add(okButton);
        form.add(checkbox);
        form.add(answer);
        form.pack();
        
        // Add background and form to the scene
        add(new FilledSprite(BLACK));
        addLayer(form);
    }
    
    public Sprite createTextFieldBackground(TextField field) {
        field.selectionColor.set(rgb(0x1d5ef2));
        ImageSprite background = new ImageSprite("textfield.png", field.x.get()-5, field.y.get());
        background.setAnchor(Sprite.WEST);
        return background;
    }
    
    @Override
    public void update(int elapsedTime) {
        if (checkbox.isClicked()) {
            double newAngle = checkbox.isSelected() ? Math.PI/16 : 0;
            form.angle.animateTo(newAngle, 500, Easing.ELASTIC_OUT);
        }
        if (okButton.isClicked()) {
            answer.setText("Hello, " + textField.getText() + "!");
            double w = answer.width.get();
            double h = answer.height.get();
            answer.scale(w*.1, h*.1, w, h, 300, Easing.BACK_OUT);
        }
        if (Input.isPressed(Input.KEY_TAB)) {
            if (textField.hasFocus()) {
                textField.setFocus(false);
                passwordField.setFocus(true);
            }
            else {
                textField.setFocus(true);
                passwordField.setFocus(false);
            }
        }
    }
}