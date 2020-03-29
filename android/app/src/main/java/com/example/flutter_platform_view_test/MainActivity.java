package com.example.flutter_platform_view_test;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.view.View;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;


@TargetApi(Build.VERSION_CODES.FROYO)
class GlRenderer implements GLSurfaceView.Renderer {
    private int width = 0;
    private int height = 0;
    private int frame = 0;

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES20.glEnable(GLES20.GL_SCISSOR_TEST);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.width = width;
        this.height = height;
    }

    private void drawRect(Rect r, int c) {
        GLES20.glViewport(r.left, r.top, r.width(), r.height());
        GLES20.glScissor(r.left, r.top, r.width(), r.height());
        GLES20.glClearColor(Color.red(c) / 255.f, Color.green(c) / 255.f, Color.blue(c) / 255.f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        drawRect(new Rect(0, 0, width, height), Color.WHITE);
        drawRect(new Rect(frame % width, 0, frame % width + 1, height), Color.BLACK);
        drawRect(new Rect(100, 100, 300, 300), frame % 2 == 0 ? Color.RED : Color.BLUE);
        frame++;
    }
}

@TargetApi(Build.VERSION_CODES.FROYO)
class GlSurface implements PlatformView {
    private final GLSurfaceView view;

    GlSurface(Context context) {
        view = new GLSurfaceView(context);
        view.setEGLContextClientVersion(2);
        view.setEGLConfigChooser(8, 8, 8, 0, 16, 0);

        view.setRenderer(new GlRenderer());
        view.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public void dispose() {
    }
}

class GlSurfaceFactory extends PlatformViewFactory {
    GlSurfaceFactory() {
        super(StandardMessageCodec.INSTANCE);
    }

    @Override
    public PlatformView create(Context context, int i, Object o) {
        return new GlSurface(context);
    }
}

class GlSurfacePlugin implements FlutterPlugin {
    @Override
    public void onAttachedToEngine(FlutterPluginBinding flutterPluginBinding) {
        flutterPluginBinding.getPlatformViewRegistry()
            .registerViewFactory("gl_surface", new GlSurfaceFactory());
    }

    @Override
    public void onDetachedFromEngine(FlutterPluginBinding flutterPluginBinding) {
    }
}


public class MainActivity extends FlutterActivity {
    @Override
    public void configureFlutterEngine(FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        flutterEngine.getPlugins().add(new GlSurfacePlugin());
    }
}
