/*
 * @(#)SVGRect.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.samples.svg.figures;

import java.awt.*;
import java.awt.geom.*;

import static org.jhotdraw.draw.AttributeKeys.TRANSFORM;

import org.jhotdraw.samples.svg.SVGAttributeKeys;

/**
 * SVGRect.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class SVGRectFigure extends SVGAttributedFigure implements SVGFigure {

    private static final long serialVersionUID = 1L;

    private static final double ACV;

    static {
        double angle = Math.PI / 4.0;
        double a = 1.0 - Math.cos(angle);
        double b = Math.tan(angle);
        double c = Math.sqrt(1.0 + b * b) - 1 + a;
        double cv = 4.0 / 3.0 * a * b / c;
        ACV = (1.0 - cv);
    }

    // Encapsulated arc dimensions
    private ArcDimensions arcDimensions;

    // Replace RoundRectangle2D with a wrapper that includes a Null Object
    private ShapeWrapper roundrect;

    private transient Shape cachedTransformedShape;
    private transient Shape cachedHitShape;

    public SVGRectFigure() {
        this(0, 0, 0, 0, new ArcDimensions(0, 0));
    }

    public SVGRectFigure(double x, double y, double width, double height) {
        this(x, y, width, height, new ArcDimensions(0, 0));
    }

    public SVGRectFigure(double x, double y, double width, double height, ArcDimensions arcDimensions) {
        this.roundrect = new ShapeWrapper(new RoundRectangle2D.Double(x, y, width, height, arcDimensions.getWidth(), arcDimensions.getHeight()));
        this.arcDimensions = arcDimensions;
        SVGAttributeKeys.setDefaults(this);
        setConnectable(false);
    }

    // DRAWING
    @Override
    protected void drawFill(Graphics2D g) {
        g.fill(roundrect.getShape());
    }

    @Override
    protected void drawStroke(Graphics2D g) {
        g.draw(roundrect.getShape());
    }

    // SHAPE AND BOUNDS
    public double getX() {
        return roundrect.getX();
    }

    public double getY() {
        return roundrect.getY();
    }

    public double getWidth() {
        return roundrect.getWidth();
    }

    public double getHeight() {
        return roundrect.getHeight();
    }

    public double getArcWidth() {
        return arcDimensions.getWidth();
    }

    public double getArcHeight() {
        return arcDimensions.getHeight();
    }

    public void setArcWidth(double newValue) {
        arcDimensions = new ArcDimensions(newValue, arcDimensions.getHeight());
        roundrect.updateArcDimensions(arcDimensions);
    }

    public void setArcHeight(double newValue) {
        arcDimensions = new ArcDimensions(arcDimensions.getWidth(), newValue);
        roundrect.updateArcDimensions(arcDimensions);
    }

    public void setArc(double width, double height) {
        arcDimensions = new ArcDimensions(width, height);
        roundrect.updateArcDimensions(arcDimensions);
    }

    @Override
    public Rectangle2D.Double getBounds() {
        return roundrect.getBounds();
    }

    @Override
    public void setBounds (Point2D.Double anchor, Point2D.Double lead){
        invalidateTransformedShape();
        roundrect.setBounds(anchor, lead);
    }
    @Override
    public Shape contains(Point2D.Double p) {
        return roundrect.getShape().contains(p);

        private void invalidateTransformedShape () {
            cachedTransformedShape = null;
            cachedHitShape = null;
        }

        // Transformed Shape Methods
        private Shape getTransformedShape () {
            if (cachedTransformedShape == null) {
                cachedTransformedShape = roundrect.getTransformedShape(get(TRANSFORM));
            }
            return cachedTransformedShape;
        }

        private Shape getHitShape () {
            if (cachedHitShape == null) {
                cachedHitShape = SVGAttributeKeys.getHitStroke(this, 1.0).createStrokedShape(getTransformedShape());
            }
            return cachedHitShape;
        }

    }
    @Override
    public boolean isEmpty () {
        // A rectangle is empty if its width or height is zero or less
        return roundrect.getWidth() <= 0 || roundrect.getHeight() <= 0;
    }

    @Override
    public Object getTransformRestoreData() {
        return null;
    }

    @Override
    public void restoreTransformTo(Object restoreData) {

    }

    @Override
    public void transform(AffineTransform tx) {

    }

    // Null Object for ShapeWrapper
    private static class ShapeWrapper {
        private RoundRectangle2D.Double shape;

        public ShapeWrapper(RoundRectangle2D.Double shape) {
            this.shape = shape;
        }

        public Shape getShape() {
            return shape != null ? shape : new Rectangle2D.Double();
        }

        public double getX() {
            return shape != null ? shape.x : 0;
        }

        public double getY() {
            return shape != null ? shape.y : 0;
        }

        public double getWidth() {
            return shape != null ? shape.width : 0;
        }

        public double getHeight() {
            return shape != null ? shape.height : 0;
        }

        public void updateArcDimensions(ArcDimensions arcDimensions) {
            if (shape != null) {
                shape.arcwidth = arcDimensions.getWidth();
                shape.archeight = arcDimensions.getHeight();
            }
        }

        public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
            if (shape != null) {
                shape.x = Math.min(anchor.x, lead.x);
                shape.y = Math.min(anchor.y, lead.y);
                shape.width = Math.max(0.1, Math.abs(lead.x - anchor.x));
                shape.height = Math.max(0.1, Math.abs(lead.y - anchor.y));
            }
        }

        public Rectangle2D.Double getBounds() {
            return shape != null ? (Rectangle2D.Double) shape.getBounds2D() : new Rectangle2D.Double();
        }

        public Shape getTransformedShape(AffineTransform transform) {
            if (transform != null) {
                return transform.createTransformedShape(getShape());
            }
            return getShape();
        }
    }

    // Encapsulated class for arc dimensions
    private static class ArcDimensions {
        private final double width;
        private final double height;

        public ArcDimensions(double width, double height) {
            this.width = width;
            this.height = height;
        }

        public double getWidth() {
            return width;
        }

        public double getHeight() {
            return height;
        }
    }
}

