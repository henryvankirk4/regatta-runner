import java.awt.*;
import java.awt.geom.AffineTransform;
import javax.swing.*;
import java.io.File;
import javax.sound.sampled.*;

public class HighDensityChip extends JPanel {
    private double x = -150; 
    private double angle = 0;
    private double scale = 1.0;
    private boolean showText = false;
    
    private final int GREEN_DIE_SIZE = 120; 
    private final int GREY_CHIP_SIZE = 103;
    
    // Internal sound player instance
    private SoundPlayer soundPlayer = new SoundPlayer();

    public HighDensityChip() {
        // Set your custom light grey background
        setBackground(new Color(210, 210, 215));
        
        new Timer(16, e -> {
            int centerX = getWidth() / 2;

            if (x < centerX) {
                x += 5; 
                angle += 0.12; 
            } else {
                x = centerX;
                angle = Math.toRadians(45); 
                
                if (scale < 1.7) {
                    scale += 0.04; 
                } else if (!showText) {
                    // Trigger sound and text state
                    soundPlayer.play("nouveau-jingle-netflix.wav");
                    showText = true;
                }
            }
            repaint();
        }).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        AffineTransform reset = g2d.getTransform();

        // Draw the Animated Chip
        g2d.translate(x, getHeight() / 2.0);
        g2d.scale(scale, scale);
        g2d.rotate(angle);

        g2d.setColor(new Color(0, 200, 100));
        g2d.fillRect(-GREEN_DIE_SIZE / 2, -GREEN_DIE_SIZE / 2, GREEN_DIE_SIZE, GREEN_DIE_SIZE);
        
        g2d.setColor(new Color(80, 80, 85));
        g2d.fillRoundRect(-GREY_CHIP_SIZE / 2, -GREY_CHIP_SIZE / 2, 
                          GREY_CHIP_SIZE, GREY_CHIP_SIZE, 12, 12);
                          
        // Draw the Branding Text
        if (showText) {
            g2d.setTransform(new AffineTransform()); 
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("SansSerif", Font.BOLD, 120)); // Adjusted size for visibility
            
            // "Prosser" - Positioned top-ish
            g2d.drawString("Prosser", 50, 150);
            
            // "Studios" - Positioned bottom-ish (Adjusted from 1100 to stay on screen)
            g2d.drawString("Studios", getWidth() - 500, getHeight() - 100);
        }

        g2d.setTransform(reset);
    }

    // Combined Sound Logic
    private class SoundPlayer {
        public void play(String fileName) {
            try {
                File f = new File(fileName);
                if (!f.exists()) return;
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}