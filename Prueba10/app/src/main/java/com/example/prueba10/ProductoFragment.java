package com.example.prueba10;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prueba10.util.Constant;
import com.example.prueba10.util.Utilidades;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Document;

import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button btn_sel_imagen;
    private Button btn_subir_imagen;
    private ImageView imv_imagen;

    private EditText edt_producto_nombre;
    private EditText edt_producto_categoria;
    private EditText edt_producto_precio;
    private CheckBox chb_producto_stock;
    private Button btn_producto_guardar;
    private Button btn_producto_ubicacion;
    private TextView tev_producto_ubicacion;

    private Uri data1;

    private final int REQUEST_FILE_CHOOSER = 1;
    private final int REQUEST_MAPA = 2;

    private SharedPreferences mispreferencias;

    String usuario;

    FirebaseFirestore db;

    String urlImage;

    private Activity miactividad;

    private Double latitud;
    private Double longitud;

    public ProductoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductoFragment newInstance(String param1, String param2) {
        ProductoFragment fragment = new ProductoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_producto, container, false);

        miactividad = getActivity();
        btn_sel_imagen = root.findViewById(R.id.btn_sel_imagen);
        btn_subir_imagen = root.findViewById(R.id.btn_subir_imagen);
        imv_imagen = root.findViewById(R.id.imv_imagen);

        edt_producto_nombre = root.findViewById(R.id.edt_producto_nombre);
        edt_producto_categoria = root.findViewById(R.id.edt_producto_categoria);
        edt_producto_precio = root.findViewById(R.id.edt_producto_precio);
        chb_producto_stock = root.findViewById(R.id.chb_producto_stock);
        btn_producto_guardar = root.findViewById(R.id.btn_producto_guardar);
        btn_producto_ubicacion = root.findViewById(R.id.btn_producto_ubicacion);
        tev_producto_ubicacion = root.findViewById(R.id.tev_producto_ubicacion);


        mispreferencias = getActivity().getSharedPreferences(Constant.PREFERENCE, MODE_PRIVATE);

        usuario = mispreferencias.getString("USUARIO", "NO HAY USUARIO");

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String strDate = sdf.format(c.getTime());
        Log.e("ID", strDate);

        btn_sel_imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Seleccione una imagen"), REQUEST_FILE_CHOOSER);
            }
        });

        btn_subir_imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });

        btn_producto_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = FirebaseFirestore.getInstance();
                String nombre = edt_producto_nombre.getText().toString();
                String categoria = edt_producto_categoria.getText().toString();
                int precio = Integer.parseInt(edt_producto_precio.getText().toString());
                boolean stock = chb_producto_stock.isChecked();

                Map<String, Object> producto = new HashMap<>();
                producto.put("nombre", nombre);
                producto.put("categoria", categoria);
                producto.put("precio", precio);
                producto.put("enstock", stock);
                producto.put("image", urlImage);
                producto.put("latitud", latitud);
                producto.put("longitud", longitud);


                db.collection("productos")
                        .add(producto)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.e("ADD_USER", "DocumentSnapshot added with ID: " + documentReference.getId());

                                edt_producto_nombre.setText("");
                                edt_producto_categoria.setText("");
                                edt_producto_precio.setText("");
                                chb_producto_stock.setChecked(false);
                                urlImage = "";

                                imv_imagen.setImageDrawable(getActivity().getDrawable(R.drawable.default_image));

                                Toast.makeText(getActivity(), "El producto ha sido creado", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("ADD_USER", "Error adding document", e);
                            }
                        });

            }
        });


        btn_producto_ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapaActivity.class);
                startActivityForResult(intent, REQUEST_MAPA);
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_FILE_CHOOSER) {
            if (resultCode == Activity.RESULT_OK) {
                data1 = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data1);
                    imv_imagen.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else if (requestCode == REQUEST_MAPA) {
            if (resultCode == Activity.RESULT_OK) {
                latitud = data.getDoubleExtra("latitud", 0);
                longitud = data.getDoubleExtra("longitud", 0);

                tev_producto_ubicacion.setText("Ubicacion: " + latitud + " - " + longitud);

            }
        }
    }

    public void uploadFile() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        //if there is a file to upload
        if (data1 != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Subiendo");
            progressDialog.show();

            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String strDate = sdf.format(c.getTime());
            String nombreImagen =strDate + ".jpg";
            StorageReference riversRef = storageReference.child(usuario + "/" + nombreImagen);
            riversRef.putFile(data1)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying a success toast
                            Toast.makeText(getActivity(), "File Uploaded ", Toast.LENGTH_LONG).show();

                            riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    urlImage = uri.toString();
                                    Log.e("URL_IMAGE", urlImage);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }
}