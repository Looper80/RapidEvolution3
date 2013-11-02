package com.mixshare.rapid_evolution.ui.widgets;

import com.mixshare.rapid_evolution.RE3Properties;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QRectF;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QBitmap;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFontMetrics;
import com.trolltech.qt.gui.QImage;
import com.trolltech.qt.gui.QLinearGradient;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QPainterPath;
import com.trolltech.qt.gui.QPen;
import com.trolltech.qt.gui.QPixmap;
import com.trolltech.qt.gui.QRadialGradient;
import com.trolltech.qt.gui.QSplashScreen;
import com.trolltech.qt.gui.QTextOption;

public class RE3SplashScreen extends QSplashScreen {

    private static final QSize SIZE = new QSize(607, 192);
    private QPixmap cleanSplash;
    private QPixmap logo;
    private QRect splashScreenRect;

    public RE3SplashScreen() {    	
        QRect r = QApplication.desktop().screenGeometry();
        splashScreenRect = new QRect(r.width() / 2 - SIZE.width() / 2,
                                     r.height() / 2 - SIZE.height() / 2,
                                     SIZE.width(), SIZE.height());

        QPixmap desktopBg = QPixmap.grabWindow(QApplication.desktop().winId(),
                                               splashScreenRect.x(),
                                               splashScreenRect.y(),
                                               splashScreenRect.width(),
                                               splashScreenRect.height());

        QImage target = new QImage(SIZE, QImage.Format.Format_RGB32);
        logo = new QPixmap(RE3Properties.getProperty("application_splashcreen_image"));

        final int margin = 20;
        final int shadow = 35;
        QRectF tr = new QRectF(0, 0, SIZE.width() - margin - shadow -1 , SIZE.height() - margin - shadow -1);

        final int round = 10;
        final int round2 = round * 2;

        QPainterPath path = new QPainterPath();
        path.moveTo(tr.left() + round, tr.top());
        path.lineTo(tr.right() - round, tr.top());
        path.arcTo(tr.right() - round2, tr.top(), round2, round2, 90, -90);
        path.lineTo(tr.right(), tr.bottom() - round);
        path.arcTo(tr.right() - round2, tr.bottom() - round2, round2, round2, 0, -90);
        path.lineTo(tr.left() - round, tr.bottom());
        path.arcTo(tr.left(), tr.bottom() - round2, round2, round2, 270, -90);
        path.lineTo(tr.left(), tr.top() - round);
        path.arcTo(tr.left(), tr.top(), round2, round2, 180, -90);

        QPainter p = new QPainter(target);
        p.setRenderHint(QPainter.RenderHint.Antialiasing);

        p.drawPixmap(0, 0, desktopBg);

        p.setClipPath(path);

        // The background blurring...
        p.save();
        p.setOpacity(0.3);
        p.drawPixmap(-1, -1, desktopBg);
        p.drawPixmap(1, 1, desktopBg);
        p.setOpacity(0.2);
        p.drawPixmap(1, -1, desktopBg);
        p.drawPixmap(-1, 1, desktopBg);
        p.setOpacity(1);

        QLinearGradient gradient = new QLinearGradient(0, 0, 0, tr.height());

        double alpha = 0.3;
        QColor whiteAlpha = QColor.fromRgbF(1, 1, 1, alpha);
        QColor whiteHighlight = QColor.fromRgbF(1, 1, 1);
        QColor whiteBorder = QColor.fromRgbF(0.5, 0.5, 0.5, 0.8);

        double highlight = 0.79;
        double size = 0.08;

        gradient.setColorAt(highlight - size, whiteAlpha);
        gradient.setColorAt(highlight - size*0.9, whiteBorder);
        gradient.setColorAt(highlight, whiteHighlight);
        gradient.setColorAt(highlight + size*0.9, whiteBorder);
        gradient.setColorAt(highlight + size, whiteAlpha);

        p.fillRect(target.rect(), new QBrush(gradient));
        p.restore();

        final QRectF rectRight = new QRectF(tr.left() - margin + tr.width() + 1,
                   tr.top() + shadow,
                   shadow, tr.height() - shadow + 1 - margin);
        final QRectF rectBottom = new QRectF(tr.left() + shadow,
                tr.top() - margin + tr.height() + 1, tr.width() - shadow + 1 - margin, shadow);
        final QRectF rectBottomRight = new QRectF(tr.left() - margin + tr.width() + 1,
                tr.top() - margin + tr.height() + 1, shadow, shadow);
        final QRectF rectTopRight = new QRectF(tr.left() - margin + tr.width() + 1,
                tr.top(), shadow, shadow);
        final QRectF rectBottomLeft = new QRectF(tr.left(),
                tr.top() - margin + tr.height() + 1, shadow, shadow);

        QPainterPath clipPath = new QPainterPath();
        clipPath.addRect(new QRectF(0, 0, SIZE.width(), SIZE.height()));
        clipPath.addPath(path);

        p.setClipPath(clipPath);

        final QColor dark = QColor.black;
        final QColor light = QColor.transparent;

        // Drop shadow: right shadow
        {
            QLinearGradient lg = new QLinearGradient(rectRight.topLeft(),
                                                     rectRight.topRight());
            lg.setColorAt(0, dark);
            lg.setColorAt(1, light);
            p.fillRect(rectRight, new QBrush(lg));
        }

        // bottom shadow
        {
            QLinearGradient lg = new QLinearGradient(rectBottom.topLeft(), rectBottom.bottomLeft());
            lg.setColorAt(0, dark);
            lg.setColorAt(1, light);
            p.fillRect(rectBottom, new QBrush(lg));
        }

        // bottom/right corner shadow
        {
            QRadialGradient g = new QRadialGradient(rectBottomRight.topLeft(), shadow);
            g.setColorAt(0, dark);
            g.setColorAt(1, light);
            p.fillRect(rectBottomRight, new QBrush(g));
        }

        // top/right corner
        {
            QRadialGradient g = new QRadialGradient(rectTopRight.bottomLeft(), shadow);
            g.setColorAt(0, dark);
            g.setColorAt(1, light);
            p.fillRect(rectTopRight, new QBrush(g));
        }

        // bottom/left corner
        {
            QRadialGradient g = new QRadialGradient(rectBottomLeft.topRight(), shadow);
            g.setColorAt(0, dark);
            g.setColorAt(1, light);
            p.fillRect(rectBottomLeft, new QBrush(g));
        }

        p.setClipPath(path);

        // The border
        for (int i=20; i>=0; i-=3) {
            int intensity = i * 255 / 20;
            QColor c = new QColor(intensity, intensity, intensity, (255 - intensity) / 3);
            p.setPen(new QPen(c, i));
            p.drawPath(path);
        }

        p.drawPixmap(0, 0, logo);

        p.end();

        cleanSplash = QPixmap.fromImage(target);
        setPixmap(cleanSplash);

        if (com.trolltech.qt.QSysInfo.macVersion() > 0) {
            QBitmap bm = new QBitmap(tr.size().toSize());
            bm.fill(QColor.color0);
            QPainter bmp = new QPainter(bm);
            bmp.translate(-tr.x(), -tr.y());
            bmp.fillPath(path, new QBrush(QColor.color1));
            bmp.end();
            setMask(bm);
        }
    }

    public QRect splashScreenRect() {
        return splashScreenRect;
    }

    public void updateProgress(String progressMessage) {
        QPixmap pm = cleanSplash.copy();
        QPainter p = new QPainter(pm);

        QFont font = p.font();
        font.setPixelSize(11);
        p.setFont(font);

        QFontMetrics metrics = new QFontMetrics(font);
        double textWidth = metrics.width(progressMessage);
        double textHeight = metrics.ascent();

        QRectF rect = new QRectF(logo.rect().adjusted(10, 0, 0, 0));
        p.drawText(rect, progressMessage, new QTextOption(new Qt.Alignment(Qt.AlignmentFlag.AlignLeft, Qt.AlignmentFlag.AlignBottom)));
        p.end();

        setPixmap(pm);
        QApplication.processEvents();
    }


}
