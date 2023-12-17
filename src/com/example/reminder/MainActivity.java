package com.example.reminder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.List;


public class MainActivity extends Activity {

    private ListView remindersListView; // Déclarer ListView
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation de remindersListView
        remindersListView = (ListView) findViewById(R.id.remindersListView);

        // Charger les données du fichier dans la ListView
        loadFileDataToListView();

        // Ajouter un listener pour un appui long sur un élément de la ListView
        remindersListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteConfirmationDialog(position);
                return true;
            }
        });

        // Vos autres initialisations de vues et d'événements ici...

        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	EditText reminderEditText = (EditText) findViewById(R.id.reminderEditText);    	
                
                String reminderText = reminderEditText.getText().toString();

                if (!reminderText.isEmpty()) {
                    if (adapter == null) {
                        adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1);
                        remindersListView.setAdapter(adapter);
                    }

                    adapter.add(reminderText);
                    adapter.notifyDataSetChanged();
                    reminderEditText.setText("");

                    // Après l'ajout de l'élément à la liste, sauvegarder la ListView dans un fichier
                    FileHelper.saveListViewToFile(MainActivity.this, remindersListView, "list_data.txt");
                }
            }
        });
        
        // Enregistrer le menu contextuel pour supprimer l'élément sélectionné
        registerForContextMenu(remindersListView);
    }

    // Charger les données du fichier dans la ListView
    private void loadFileDataToListView() {
        List<String> dataList = FileHelper.readFileToList(MainActivity.this, "list_data.txt");
        if (dataList != null && dataList.size() > 0) {
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
            remindersListView.setAdapter(adapter);
        }
    }

    // Afficher une boîte de dialogue de confirmation de suppression
    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Voulez-vous supprimer cet élément ?");
        builder.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Supprimer l'élément sélectionné de la ListView et du fichier
                if (adapter != null) {
                    adapter.remove(adapter.getItem(position));
                    adapter.notifyDataSetChanged();
                    FileHelper.saveListViewToFile(MainActivity.this, remindersListView, "list_data.txt");
                }
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    // Créer un menu contextuel pour la suppression d'éléments
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Options");
        menu.add(0, v.getId(), 0, "Supprimer");
    }

    // Gérer la sélection d'éléments dans le menu contextuel
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Supprimer") {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int position = info.position;
            showDeleteConfirmationDialog(position);
        }
        return true;
    }
}
