package net.rodja.edittextvalueproblemdemo;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

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
                sleep(1);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        simulateUserInput((ViewGroup) findViewById(R.id.fragment));
                    }
                });

                sleep(2);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        simulateUserInput((ViewGroup) findViewById(R.id.fragment));
                        getFragmentManager().beginTransaction().detach(fragment).commit();
                    }
                });

                sleep(2);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getFragmentManager().beginTransaction().attach(fragment).commit();
                    }
                });

            }

            private void sleep(int seconds) {
                try {
                    Thread.sleep(seconds * 1000);
                } catch (InterruptedException e) {
                }
            };
        }.start();
    }

    int input = 0;

    private void simulateUserInput(ViewGroup v) {
        for (int i = 0; i < v.getChildCount(); i++) {
            if (v.getChildAt(i) instanceof ViewGroup)
                simulateUserInput((ViewGroup) v.getChildAt(i));
            else if (v.getChildAt(i) instanceof EditText) {
                ((EditText) v.getChildAt(i)).setText("input #" + input++);
            }

        }
    }

    public static class DemoFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            Log.v("DemoFragment", "onCreateView");

            View content = inflater.inflate(R.layout.fragment_view, container, false);
            ListView list = (ListView) content.findViewById(R.id.list);
            list.setAdapter(new ItemsAdapter());
            return content;
        }

        private class ItemsAdapter extends ArrayAdapter<String> {
            public ItemsAdapter() {
                super(getActivity(), R.layout.editable_item);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.editable_item, null, false);
                } else {
                }
                EditText editText = (EditText) convertView.findViewById(R.id.editText);
                editText.setText("position: " + position);

                return convertView;
            }

            @Override
            public int getCount() {
                return 5;
            }
        }

    }

}
