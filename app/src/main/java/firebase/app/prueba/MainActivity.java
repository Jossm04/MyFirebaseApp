package firebase.app.prueba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import firebase.app.prueba.model.Persona;

public class MainActivity extends AppCompatActivity {
    private List<Persona> personaList= new ArrayList<Persona>();
ArrayAdapter<Persona> personaArrayAdapter;
EditText nomP, appP, correoP, passwordP;
ListView listV;

FirebaseDatabase fireDatabase;
DatabaseReference databaseReference;
Persona personaSelected;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nomP = findViewById(R.id.txtNombre );
        appP= findViewById(R.id.txtApellidos);
        correoP=findViewById(R.id.txtCorreo);
        passwordP=findViewById(R.id.txtPassw);

        listV=findViewById(R.id.lvl);
        inicializarFirebase();
         listaDatos();
listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        personaSelected=(Persona)parent.getItemAtPosition(position);
                nomP.setText(personaSelected.getNombre());
        appP.setText(personaSelected.getApellidos());
        correoP.setText(personaSelected.getCorreo());
       passwordP.setText(personaSelected.getPassword());
    }
});



    }

    private void listaDatos() {
databaseReference.child("Persona").addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        personaList.clear();
        for (DataSnapshot objSanpshot : dataSnapshot.getChildren()){
            Persona p =objSanpshot.getValue(Persona.class);
            personaList.add(p);
            personaArrayAdapter= new ArrayAdapter<Persona>(MainActivity.this, android.R.layout.simple_list_item_1,personaList);
            listV.setAdapter(personaArrayAdapter);
        }

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});


    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        fireDatabase=FirebaseDatabase .getInstance();
        fireDatabase.setPersistenceEnabled((true));
        databaseReference=fireDatabase.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu .menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        String nombre = nomP.getText().toString();
        String apellidos = appP.getText().toString();
        String correo = correoP.getText().toString();
        String pass = passwordP.getText().toString();

        switch (item.getItemId()) {
            case R.id.icon_add: {
                if (nombre.equals("") || apellidos.equals("") || correo.equals("") || pass.equals("")) {
                    validacion();
                } else {
                    Persona p = new Persona();
                    p.setUid(UUID.randomUUID().toString());
                    p.setNombre(nombre);
                    p.setApellidos(apellidos);
                    p.setCorreo(correo);
                    p.setPassword(pass);
                    databaseReference.child("Persona").child(p.getUid()).setValue(p);
                    Toast.makeText(this, "Agregar", Toast.LENGTH_LONG).show();
                    LimpiarCajas();

                }
                break;
            }
            case R.id.icon_save: {
                Persona p = new Persona();
                p.setUid(personaSelected.getUid());
                p.setNombre(nomP.getText().toString().trim());
                p.setApellidos(appP.getText().toString().trim());
                p.setCorreo(correoP.getText().toString().trim());
                p.setPassword(passwordP.getText().toString().trim());
                databaseReference.child("Persona").child(p.getUid()).setValue(p);
                Toast.makeText(this, "Guardar", Toast.LENGTH_LONG).show();
                LimpiarCajas();
                break;
            }
            case R.id.icon_delete: {
                Persona p = new Persona();
                p.setUid(personaSelected.getUid());
                p.setNombre(nomP.getText().toString());
                p.setApellidos(appP.getText().toString());
                p.setCorreo(correoP.getText().toString());
                p.setPassword(passwordP.getText().toString());
                databaseReference.child("Persona").child(p.getUid()).removeValue();
                LimpiarCajas();
                Toast.makeText(this, "Eliminar", Toast.LENGTH_LONG).show();
                break;
            }
            default:
                break;
        }
                return true;
            }

    private void LimpiarCajas() {
        nomP.setText("");
        appP.setText("");
        correoP.setText("");
        passwordP.setText("");

    }

    private void validacion() {
        String nombre = nomP.getText().toString();
        if (nombre.equals((""))){
            nomP.setError("Required");
        }
    }

}
