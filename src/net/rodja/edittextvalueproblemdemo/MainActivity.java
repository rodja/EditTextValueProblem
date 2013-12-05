package net.rodja.edittextvalueproblemdemo;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends Activity {

    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment = new DemoFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        getFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();

        new Thread() {
            public void run() {

                sleep();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getFragmentManager().beginTransaction().detach(fragment).commit();
                    }
                });

                sleep();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getFragmentManager().beginTransaction().attach(fragment).commit();
                    }
                });

            }

            private void sleep() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
            };
        }.start();
    }

    public static class DemoFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            Log.v("DemoFragment", "onCreateView");
            return inflater.inflate(R.layout.fragment_view, container, false);
        }
    }
}
