package models;

import enums.stakes.ColorStake;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * JPanel responsible for drawing and animating the 50-pocket roulette wheel.
 * Includes physics simulation for the spinning wheel and balls.
 */
public class Board extends JPanel {
    // ▶ Attributes ─────────────────────────────────────────────────────────────────────────────────────────────
    private final Table table;
    private double wheelAngle, ball1Angle, ball2Angle;
    private double wheelVelocity, ball1Velocity, ball2Velocity;
    public boolean ball1Stopped, ball2Stopped, wheelStopped;
    private Timer animationTimer;
    private Pocket[] results = null;

    private double ball1TargetAngle;
    private double ball2TargetAngle;

    // Physics and Calibration Constants (AJUSTADOS: RUEDA SE DETIENE PRIMERO)
    private final double BASE_WHEEL_VELOCITY = 30.0;
    private final double WHEEL_FRICTION = 0.965;
    private final double BALL_FRICTION = 0.985;
    private final double VELOCITY_THRESHOLD = 0.01;

    private final double POINTER_ANGLE = 90.0; // Top center reference angle

    private final int BALL_SIZE = 24;
    private final int HALF_BALL_SIZE = BALL_SIZE / 2;

    private static final Map<ColorStake, java.awt.Color> COLOR_MAP = new HashMap<>();

    private final java.awt.Color WOOD_DARK = new java.awt.Color(70, 35, 0);
    private final java.awt.Color WOOD_LIGHT = new java.awt.Color(120, 60, 0);
    private final java.awt.Color METAL_DARK = new java.awt.Color(60, 60, 60);
    private final java.awt.Color METAL_LIGHT = new java.awt.Color(180, 180, 180);
    private final java.awt.Color METAL_HIGHLIGHT = new java.awt.Color(230, 230, 230);

    static {
        // Colores mapeados desde ColorStake.
        COLOR_MAP.put(ColorStake.RED, new java.awt.Color(220, 20, 60));
        COLOR_MAP.put(ColorStake.ORANGE, new java.awt.Color(170, 75, 0));
        COLOR_MAP.put(ColorStake.YELLOW, new java.awt.Color(255, 215, 0));
        COLOR_MAP.put(ColorStake.GREEN, new java.awt.Color(0, 128, 0));
        COLOR_MAP.put(ColorStake.BLUE, new java.awt.Color(0, 100, 180));
        COLOR_MAP.put(ColorStake.VIOLET, new java.awt.Color(138, 43, 226));
        COLOR_MAP.put(ColorStake.BLACK, new java.awt.Color(20, 20, 20));
    }

    // ▶ Constructors ───────────────────────────────────────────────────────────────────────────────────────────
    public Board(Table table) {
        this.table = table;
        setPreferredSize(new Dimension(850, 850));
        setOpaque(false);
    }

    // ▶ Getters ────────────────────────────────────────────────────────────────────────────────────────────────
    public Pocket[] getResults() { return results; }
    public boolean isWheelStopped() { return wheelStopped; }
    public boolean isBall1Stopped() { return ball1Stopped; }
    public boolean isBall2Stopped() { return ball2Stopped; }

    // ▶ Methods ────────────────────────────────────────────────────────────────────────────────────────────────

    private double calculateTotalAngle(double initialVelocity, double friction) {
        double totalAngle = 0;
        double v = initialVelocity;
        while (Math.abs(v) > VELOCITY_THRESHOLD) {
            totalAngle += v;
            v *= friction;
        }
        return totalAngle;
    }

    private double calculatePocketCenterAngle(int pocketIndex, int numPockets) {
        double anglePerPocket = 360.0 / numPockets;
        double angleForCenter = POINTER_ANGLE + (pocketIndex * anglePerPocket) + (anglePerPocket / 2);
        return ((angleForCenter % 360) + 360) % 360;
    }

    private double calculateInitialVelocity(double targetDistance, double friction) {
        double estimatedVelocity = targetDistance * (1 - friction);
        for (int i = 0; i < 5; i++) {
            double calculatedDistance = calculateTotalAngle(estimatedVelocity, friction);
            double error = targetDistance - calculatedDistance;
            estimatedVelocity += error * (1 - friction);
            if (Math.abs(error) < 0.00001) break;
        }
        return estimatedVelocity;
    }

    /** Starts the wheel and ball spin animation based on calculated targets. */
    public void startSpin() {
        List<Pocket> pockets = table.getPockets();
        int numPockets = pockets.size();

        // 1. DETERMINE RANDOM WINNERS
        int winnerIndex1 = (int) (Math.random() * numPockets);
        int winnerIndex2 = (int) (Math.random() * numPockets);

        this.results = new Pocket[2];
        this.results[0] = pockets.get(winnerIndex1);
        this.results[1] = pockets.get(winnerIndex2);

        // 2. CALCULATE TOTAL BRAKING DISTANCE AND WHEEL CALIBRATION
        double totalBaseAngle = calculateTotalAngle(-BASE_WHEEL_VELOCITY, WHEEL_FRICTION);
        int numTurns = 3 + (int)(Math.random() * 2);
        double totalTargetAngle = -numTurns * 360.0; // Negative for anti-clockwise spin

        // 3. FINAL ABSOLUTE TARGET POSITIONS (Balls)
        double pocket1CenterAngle = calculatePocketCenterAngle(winnerIndex1, numPockets);
        double pocket2CenterAngle = calculatePocketCenterAngle(winnerIndex2, numPockets);

        ball1TargetAngle = pocket1CenterAngle;
        ball2TargetAngle = pocket2CenterAngle;

        // Add offset if results are identical to prevent visual overlap
        if (winnerIndex1 == winnerIndex2) {
            double slightOffset = 0.5;
            ball2TargetAngle = (ball2TargetAngle + slightOffset) % 360;
        }

        // 4. CALCULATE VELOCITIES
        wheelVelocity = -BASE_WHEEL_VELOCITY * (totalTargetAngle / totalBaseAngle);

        double distanceBall1 = ball1TargetAngle - wheelAngle;
        double distanceBall2 = ball2TargetAngle - wheelAngle;

        double totalBallDistance1 = totalTargetAngle + distanceBall1;
        double totalBallDistance2 = totalTargetAngle + distanceBall2;

        // Aseguramos que la bola gire al menos una vuelta más que la rueda.
        double extraTurns = 1.0;
        totalBallDistance1 += extraTurns * 360.0;
        totalBallDistance2 += extraTurns * 360.0;

        ball1Velocity = calculateInitialVelocity(totalBallDistance1, BALL_FRICTION);
        ball2Velocity = calculateInitialVelocity(totalBallDistance2, BALL_FRICTION);

        // 5. INITIALIZATION
        ball1Stopped = false;
        ball2Stopped = false;
        wheelStopped = false;

        wheelAngle = 0;
        ball1Angle = 0;
        ball2Angle = 0;

        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        animationTimer = new Timer(16, e -> updateFrame());
        animationTimer.start();
    }

    /** Updates the position of the wheel and balls during the spin. */
    public void updateFrame() {
        if (!wheelStopped) {
            wheelAngle += wheelVelocity;
            wheelVelocity *= WHEEL_FRICTION;
            if (Math.abs(wheelVelocity) < VELOCITY_THRESHOLD) { wheelVelocity = 0; wheelStopped = true; }
        }

        if (!ball1Stopped) {
            ball1Angle += ball1Velocity;
            ball1Velocity *= BALL_FRICTION;
            if (Math.abs(ball1Velocity) < VELOCITY_THRESHOLD) {
                ball1Velocity = 0;
                ball1Angle = ball1TargetAngle;
                ball1Stopped = true;
            }
        }

        if (!ball2Stopped) {
            ball2Angle += ball2Velocity;
            ball2Velocity *= BALL_FRICTION;
            if (Math.abs(ball2Velocity) < VELOCITY_THRESHOLD) {
                ball2Velocity = 0;
                ball2Angle = ball2TargetAngle;
                ball2Stopped = true;
            }
        }

        wheelAngle = ((wheelAngle % 360) + 360) % 360;
        ball1Angle = ((ball1Angle % 360) + 360) % 360;
        ball2Angle = ((ball2Angle % 360) + 360) % 360;

        repaint();

        if (wheelStopped && ball1Stopped && ball2Stopped) {
            animationTimer.stop();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);


        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int margin = 50;
        int outerRadius = Math.min(getWidth(), getHeight()) / 2 - margin;
        int innerRadius = (int) (outerRadius * 0.35);

        // --- Structure and Borders ---
        int metalOuterRadius = outerRadius + 20;
        int metalMiddleRadius = outerRadius + 10;
        int metalInnerRadius = outerRadius + 5;

        g2d.setColor(METAL_DARK);
        g2d.fill(new java.awt.geom.Ellipse2D.Double(centerX - metalOuterRadius, centerY - metalOuterRadius, metalOuterRadius * 2, metalOuterRadius * 2));
        g2d.setColor(METAL_HIGHLIGHT);
        g2d.fill(new java.awt.geom.Ellipse2D.Double(centerX - metalMiddleRadius, centerY - metalMiddleRadius, metalMiddleRadius * 2, metalMiddleRadius * 2));
        g2d.setColor(WOOD_DARK);
        g2d.fill(new java.awt.geom.Ellipse2D.Double(centerX - metalInnerRadius, centerY - metalInnerRadius, metalInnerRadius * 2, metalInnerRadius * 2));

        g2d.setColor(WOOD_DARK);
        g2d.fill(new java.awt.geom.Ellipse2D.Double(centerX - innerRadius, centerY - innerRadius, innerRadius * 2, innerRadius * 2));
        g2d.setPaint(new RadialGradientPaint(
                centerX, centerY, innerRadius, new float[]{0.0f, 0.5f, 1.0f}, new java.awt.Color[]{WOOD_LIGHT.brighter(), WOOD_LIGHT, WOOD_DARK.darker()}
        ));
        g2d.fill(new java.awt.geom.Ellipse2D.Double(centerX - innerRadius, centerY - innerRadius, innerRadius * 2, innerRadius * 2));


        // 1. DRAW ROTATING WHEEL
        Graphics2D g2dWheel = (Graphics2D) g2d.create();
        double wheelAngleNormalized = wheelAngle % 360;
        if (wheelAngleNormalized < 0) wheelAngleNormalized += 360;
        g2dWheel.rotate(Math.toRadians(wheelAngleNormalized), centerX, centerY);

        List<Pocket> pockets = table.getPockets();
        double anglePerPocket = 360.0 / pockets.size();

        int segmentInnerRadius = innerRadius + 15;
        int segmentOuterRadius = outerRadius - 15;

        for (int i = 0; i < pockets.size(); i++) {
            Pocket p = pockets.get(i);

            double pocketCenterAngle = POINTER_ANGLE + (i * anglePerPocket) + (anglePerPocket / 2);
            double startAngle = pocketCenterAngle - (anglePerPocket / 2);

            java.awt.Color pocketColor = getAWTColor(p.getColor());
            g2dWheel.setColor(pocketColor);

            Path2D.Double segment = new Path2D.Double();
            double angle1 = Math.toRadians(startAngle);
            double angle2 = Math.toRadians(startAngle + anglePerPocket);

            segment.moveTo(centerX + segmentInnerRadius * Math.cos(angle1), centerY - segmentInnerRadius * Math.sin(angle1));
            segment.lineTo(centerX + segmentOuterRadius * Math.cos(angle1), centerY - segmentOuterRadius * Math.sin(angle1));
            segment.lineTo(centerX + segmentOuterRadius * Math.cos(angle2), centerY - segmentOuterRadius * Math.sin(angle2));
            segment.lineTo(centerX + segmentInnerRadius * Math.cos(angle2), centerY - segmentInnerRadius * Math.sin(angle2));
            g2dWheel.fill(segment);

            g2dWheel.setColor(java.awt.Color.BLACK);
            g2dWheel.setStroke(new BasicStroke(1.5f));
            g2dWheel.draw(segment);

            // Draw text
            double textAngle = Math.toRadians(pocketCenterAngle);
            double textRadius = segmentOuterRadius - 20;
            int textX = (int) (centerX + Math.cos(textAngle) * textRadius);
            int textY = (int) (centerY - Math.sin(textAngle) * textRadius);
            String text = String.valueOf(p.getNumber());

            g2dWheel.setFont(new Font("Times New Roman", Font.BOLD, 26));
            g2dWheel.setColor(java.awt.Color.WHITE);

            FontMetrics fm = g2dWheel.getFontMetrics();
            g2dWheel.drawString(text, textX - fm.stringWidth(text) / 2, textY + fm.getAscent() / 3);
        }
        g2dWheel.dispose();


        // 2. DRAW POINTER AND CENTRAL DECORATION
        int centralMetalRingOuterRadius = innerRadius - 5;
        int centralMetalRingInnerRadius = centralMetalRingOuterRadius - 10;
        g2d.setColor(METAL_LIGHT);
        g2d.fill(new java.awt.geom.Ellipse2D.Double(centerX - centralMetalRingOuterRadius, centerY - centralMetalRingOuterRadius, centralMetalRingOuterRadius * 2, centralMetalRingOuterRadius * 2));
        g2d.setColor(WOOD_DARK);
        g2d.fill(new java.awt.geom.Ellipse2D.Double(centerX - centralMetalRingInnerRadius, centerY - centralMetalRingInnerRadius, centralMetalRingInnerRadius * 2, centralMetalRingInnerRadius * 2));

        // Draw pointer (top center)
        g2d.setColor(java.awt.Color.YELLOW);
        Polygon pointer = new Polygon();
        pointer.addPoint(centerX, centerY - metalOuterRadius - 10);
        pointer.addPoint(centerX - 10, centerY - metalOuterRadius + 15);
        pointer.addPoint(centerX + 10, centerY - metalOuterRadius + 15);
        g2d.fill(pointer);


        // 3. DRAW BALLS
        double b1Normalized = ball1Angle % 360;
        if (b1Normalized < 0) b1Normalized += 360;
        double b2Normalized = ball2Angle % 360;
        if (b2Normalized < 0) b2Normalized += 360;

        double b1Rad = Math.toRadians(b1Normalized);
        double b2Rad = Math.toRadians(b2Normalized);

        // Posicionamiento de las bolas (mitad interior del segmento, lejos de los números)
        int segmentMiddleRadius = (segmentInnerRadius + segmentOuterRadius) / 2;
        int ballRadiusStop = segmentMiddleRadius;

        int ballRadius1 = ballRadiusStop - (BALL_SIZE / 4);
        int ballRadius2 = ballRadiusStop + (BALL_SIZE / 4);

        // Ball 1 (White, smooth)
        int b1X = (int) (centerX + ballRadius1 * Math.cos(b1Rad));
        int b1Y = (int) (centerY - ballRadius1 * Math.sin(b1Rad));
        g2d.setColor(java.awt.Color.WHITE);
        g2d.fillOval(b1X - HALF_BALL_SIZE, b1Y - HALF_BALL_SIZE, BALL_SIZE, BALL_SIZE);

        // Ball 2 (White, smooth)
        int b2X = (int) (centerX + ballRadius2 * Math.cos(b2Rad));
        int b2Y = (int) (centerY - ballRadius2 * Math.sin(b2Rad));
        g2d.setColor(java.awt.Color.WHITE);
        g2d.fillOval(b2X - HALF_BALL_SIZE, b2Y - HALF_BALL_SIZE, BALL_SIZE, BALL_SIZE);

        // 4. DISPLAY RESULTS
        if (wheelStopped && ball1Stopped && ball2Stopped) {
            String resStr = results[0].getNumber() + " | " + results[1].getNumber();
            g2d.setColor(java.awt.Color.YELLOW);
            g2d.setFont(new Font("ARIAL", Font.BOLD, 24));
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(resStr, centerX - fm.stringWidth(resStr) / 2, centerY + 8);
        }

        g2d.dispose();
    }

    /** Maps the ColorStake enum to a java.awt.Color for drawing. */
    private java.awt.Color getAWTColor(ColorStake colorStake) {
        return COLOR_MAP.getOrDefault(colorStake, java.awt.Color.GRAY);
    }
}