package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.splashscreen.view;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.renderscript.Float3;
import android.renderscript.Matrix4f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.R;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.splashscreen.glkits.BufferUtils;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.splashscreen.glkits.ShaderProgram;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.splashscreen.glkits.ShaderUtils;

public class OpenGLRender implements GLSurfaceView.Renderer {
//    Animation mAnimation = new TranslateAnimation(300f, -300f, 0.0f, 0.0f);
//    public static int[] textures = new int[2];
    private static final float ONE_SEC = 1000.0f; // 1 second

    private Context context;
    private Cube   cube;
    private long lastTimeMillis = 0L;

    public OpenGLRender(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig) {
        ShaderProgram shader = new ShaderProgram(
                ShaderUtils.readShaderFileFromRawResource(context, R.raw.simple_vertex_shader),
                ShaderUtils.readShaderFileFromRawResource(context, R.raw.simple_fragment_shader)
        );

        cube = new Cube(shader);
        cube.setPosition(new Float3(0.0f, 0.0f, 0.0f));

        lastTimeMillis = System.currentTimeMillis();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        Matrix4f perspective = new Matrix4f();
        perspective.loadPerspective(85.0f, (float)width / (float)height, 1.0f, -150.0f);

        if(cube != null) {
            cube.setProjection(perspective);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        long currentTimeMillis = System.currentTimeMillis();
        updateWithDelta(currentTimeMillis - lastTimeMillis);
        lastTimeMillis = currentTimeMillis;

    }

    public void updateWithDelta(long dt) {

        Matrix4f camera2 = new Matrix4f();
        camera2.translate(0.0f, 0.0f, -5.0f);
        cube.setCamera(camera2);
        cube.setRotationY((float)( cube.rotationY + Math.PI * dt / (ONE_SEC * 0.1f) ));
        cube.setRotationZ((float)( cube.rotationZ + Math.PI * dt / (ONE_SEC * 0.1f) ));
        cube.draw(dt);
    }

    static class Model {
        private static final int COORDS_PER_VERTEX = 3;
        private static final int COLORS_PER_VERTEX = 4;
        private static final int SIZE_OF_FLOAT = 4;
        private static final int SIZE_OF_SHORT = 2;

        private ShaderProgram shader;
        private String name;
        private float vertices[];
        private short indices[];


        private FloatBuffer vertexBuffer;
        private int vertexBufferId;
        private int vertexStride;

        private ShortBuffer indexBuffer;
        private int indexBufferId;

        // ModelView Transformation
        protected Float3 position = new Float3(0f, 0f, 0f);
        protected float rotationX  = 0.0f;
        protected float rotationY  = 0.0f;
        protected float rotationZ  = 0.0f;
        protected float scale      = 1.0f;
        protected Matrix4f camera  = new Matrix4f();
        protected Matrix4f projection = new Matrix4f();

        public Model(String name, ShaderProgram shader, float[] vertices, short[] indices) {
            this.name = name;
            this.shader = shader;
            this.vertices = Arrays.copyOfRange(vertices, 0, vertices.length);
            this.indices = Arrays.copyOfRange(indices, 0, indices.length);

            setupVertexBuffer();
            setupIndexBuffer();
        }

        public void setPosition(Float3 position) {
            this.position = position;
        }

        public void setRotationX(float rotationX) {
            this.rotationX = rotationX;
        }

        public void setRotationY(float rotationY) {
            this.rotationY = rotationY;
        }

        public void setRotationZ(float rotationZ) {
            this.rotationZ = rotationZ;
        }

        public void setScale(float scale) {
            this.scale = scale;
        }

        private void setupVertexBuffer() {
            vertexBuffer = BufferUtils.newFloatBuffer(vertices.length);
            vertexBuffer.put(vertices);
            vertexBuffer.position(0);

            IntBuffer buffer = IntBuffer.allocate(1);
            GLES20.glGenBuffers(1, buffer);
            vertexBufferId = buffer.get(0);
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferId);
            GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertices.length * SIZE_OF_FLOAT, vertexBuffer, GLES20.GL_STATIC_DRAW);
            vertexStride = (COORDS_PER_VERTEX + COLORS_PER_VERTEX) * SIZE_OF_FLOAT; // 4 bytes per vertex
        }

        private void setupIndexBuffer() {
            // initialize index short buffer for index
            indexBuffer = BufferUtils.newShortBuffer(indices.length);
            indexBuffer.put(indices);
            indexBuffer.position(0);

            IntBuffer buffer = IntBuffer.allocate(1);
            GLES20.glGenBuffers(1, buffer);
            indexBufferId = buffer.get(0);
            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBufferId);
            GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, indices.length * SIZE_OF_SHORT, indexBuffer, GLES20.GL_STATIC_DRAW);
        }

        public Matrix4f modelMatrix() {
            Matrix4f mat = new Matrix4f(); // make a new identitiy 4x4 matrix
            mat.translate(position.x, position.y, position.z);
            mat.rotate(rotationX, 1.0f, 0.0f, 0.0f);
            mat.rotate(rotationY, 0.0f, 1.0f, 0.0f);
            mat.rotate(rotationZ, 0.0f, 0.0f, 1.0f);
            mat.scale(scale, scale, scale);
            return mat;
        }

        public void setCamera(Matrix4f mat) {
            camera.load(mat);
        }

        public void setProjection(Matrix4f mat) {
            projection.load(mat);
        }

        public void draw(long dt) {

            shader.begin();

            camera.multiply(modelMatrix());
            shader.setUniformMatrix("u_ProjectionMatrix", projection);
            shader.setUniformMatrix("u_ModelViewMatrix",  camera);
            shader.enableVertexAttribute("a_Position");
            shader.setVertexAttribute("a_Position", COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, 0);

            shader.enableVertexAttribute("a_Color");
            shader.setVertexAttribute("a_Color", COLORS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, COORDS_PER_VERTEX * SIZE_OF_FLOAT);

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferId);
            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBufferId);
            GLES20.glDrawElements(
                    GLES20.GL_TRIANGLES,        // mode
                    indices.length,             // count
                    GLES20.GL_UNSIGNED_SHORT,   // type
                    0);                         // offset

            shader.disableVertexAttribute("a_Position");
            shader.disableVertexAttribute("a_Color");

            shader.end();
        }
    }

    static class Cube extends Model {
        static final float vertices[] = {
                // Front
                1.0f, -1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f,       // 0
                1.0f,  1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f,       // 1
                -1.0f,  1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f,       // 2
                -1.0f, -1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f,       // 3


                // Back
                -1.0f, -1.0f, -1.0f, 1.0f, 0.0f, 0.0f, 1.0f,     // 4
                -1.0f,  1.0f, -1.0f, 1.0f, 0.0f, 0.0f, 1.0f,     // 5
                1.0f,  1.0f, -1.0f, 0.0f, 1.0f, 0.0f, 1.0f,     // 6
                1.0f, -1.0f, -1.0f, 0.0f, 1.0f, 0.0f, 1.0f      // 7
        };

        static final short indices[] = {
                // Front
                0, 1, 2,
                2, 3, 0,

                // Back
                4, 5, 6,
                6, 7, 4,

                // Left
                3, 2, 5,
                5, 4, 3,

                // Right
                7, 6, 1,
                1, 0, 7,

                // Top
                1, 6, 5,
                5, 2, 1,

                // Bottom
                3, 4, 7,
                7, 0, 3
        };

        public Cube(ShaderProgram shader) {
            super("cube", shader, vertices, indices);
        }
    }

//    private void drawText(GL10 gl) {
//        Bitmap bitmap = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_4444);
//        Canvas canvas = new Canvas(bitmap);
//        bitmap.eraseColor(0);
//
//        Paint textPaint = new Paint();
//        textPaint.setTextSize(200);
//        textPaint.setAntiAlias(true);
//        textPaint.setARGB(0xff, 0x00, 0x00, 0x00);
//        canvas.drawText("NOTIFIME", 16,112, textPaint);
//
//        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);gl.glGenTextures(1, textures, 0);
////...and bind it to our array
//        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
//
////Create Nearest Filtered Texture
//        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
//        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
//
////Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
//        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
//        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
//
////Use the Android GLUtils to specify a two-dimensional texture image from our bitmap
//        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
//
//        System.out.println("DRAWED");
////        bitmap.recycle();
//    }
}
