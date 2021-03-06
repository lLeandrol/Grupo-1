package com.example.prueba10.ui.gallery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prueba10.DetalleProductoActivity;
import com.example.prueba10.R;
import com.example.prueba10.util.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GalleryFragment extends Fragment {

    private RecyclerView rev_productos;
    private RecyclerView.Adapter mAdapter;
    private RatingBar rab_estrella;
    private Spinner spn_categorias;
    private TextView tev_gallery;
    private String[] categorias = new String[]{ "Futbol", "Baloncesto", "Tenis", "Golf", "Natación"};

    private String productos = "[{\"nombre\":\"Balon\",\"categoria\":\"Futbol\",\"precio\":40000,\"enstock\":true,\"image\":\"https://www.crushpixel.com/big-static12/preview4/soccer-ball-with-digital-internet-1002985.jpg\",\"sucursal\":[{\"nombre\":\"Sucursal A\",\"direccion\":\"Direccion A\",\"encargado\":{\"nombre\":\"Encargado A\",\"celular\":\"31425321\"}},{\"nombre\":\"Sucursal B\",\"direccion\":\"Direccion B\",\"encargado\":{\"nombre\":\"Encargado B\",\"celular\":\"31425321\"}}]},{\"nombre\":\"Guayos\",\"categoria\":\"Futbol\",\"precio\":50000,\"enstock\":true,\"image\":\"https://i.pinimg.com/550x/7b/10/be/7b10befbf264a22fe27a6f9bbf8aab73.jpg\",\"sucursal\":[{\"nombre\":\"Sucursal C\",\"direccion\":\"Direccion C\",\"encargado\":{\"nombre\":\"Encargado C\",\"celular\":\"31425321\"}},{\"nombre\":\"Sucursal D\",\"direccion\":\"Direccion D\",\"encargado\":{\"nombre\":\"Encargado D\",\"celular\":\"31425321\"}}]},{\"nombre\":\"Guantes\",\"categoria\":\"Futbol\",\"precio\":200000,\"enstock\":true,\"image\":\"https://contents.mediadecathlon.com/p1706042/k$ee991113590f376ce4eccf1f6a8c2b7a/guantes-para-portero-f500-ninos-azul-y-amarillo.jpg\",\"sucursal\":[{\"nombre\":\"Sucursal A\",\"direccion\":\"Direccion A\",\"encargado\":{\"nombre\":\"Encargado A\",\"celular\":\"31425321\"}},{\"nombre\":\"Sucursal B\",\"direccion\":\"Direccion B\",\"encargado\":{\"nombre\":\"Encargado B\",\"celular\":\"31425321\"}}]}]";

    FirebaseFirestore db;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        tev_gallery = root.findViewById(R.id.tev_gallery);
        spn_categorias = root.findViewById(R.id.spn_categorias);
        rab_estrella = root.findViewById(R.id.rab_estrella);
        rev_productos = root.findViewById(R.id.rev_productos);

        tev_gallery.setText("HOLA MUNDO GALLERY");

        // Opción 1: ARRAY JAVA
        //ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, categorias);


        //Opción 2: STRING-ARRAY del XML de recursos
        ArrayAdapter<CharSequence> adaptador = ArrayAdapter.createFromResource(getActivity(), R.array.categorias, android.R.layout.simple_spinner_dropdown_item);

        spn_categorias.setAdapter(adaptador);

        spn_categorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //tev_gallery.setText(categorias[position]);

                String categoria = spn_categorias.getSelectedItem().toString();
                tev_gallery.setText(categoria);
                //Toast.makeText(getActivity(), "Opcioón seleccionada: " + categoria, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rab_estrella.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                Toast.makeText(getActivity(), "RATING: " + rating, Toast.LENGTH_SHORT).show();
            }
        });


        rev_productos.setLayoutManager(new LinearLayoutManager(getActivity()));


        //rev_productos.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        try {
            JSONArray jsonProductos = new JSONArray(productos);

            //mAdapter = new ProductosAdapter(jsonProductos, getActivity());
            //rev_productos.setAdapter(mAdapter);

            JSONObject producto0 = jsonProductos.getJSONObject(0);

            String nombre = producto0.getString("nombre");

            JSONArray sucursales = producto0.getJSONArray("sucursal");


            JSONObject sucursal1 = sucursales.getJSONObject(1);

            String nombreSucursal = sucursal1.getString("nombre");


            Toast.makeText(getActivity(), "Nombre: " + nombreSucursal, Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        db = FirebaseFirestore.getInstance();
        db.collection("productos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            JSONArray productos = new JSONArray();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.e("TAG", document.getId() + " => " + document.getData());

                                JSONObject producto = new JSONObject();
                                try {
                                    producto.put("codigo", document.getId());
                                    producto.put("nombre", document.getData().get("nombre"));
                                    producto.put("categoria", document.getData().get("categoria"));
                                    producto.put("precio", document.getData().get("precio"));
                                    producto.put("enstock", document.getData().get("enstock"));

                                    if (document.getData().get("image") != null) {
                                        producto.put("image", document.getData().get("image"));
                                    } else {
                                        producto.put("image", "error");
                                    }

                                    if (document.getData().get("latitud") != null && document.getData().get("longitud") != null) {
                                        producto.put("latitud", document.getData().get("latitud"));
                                        producto.put("longitud", document.getData().get("longitud"));
                                    } else {
                                        producto.put("latitud", 0.0);
                                        producto.put("longitud", 0.0);
                                    }




                                    productos.put(producto);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            mAdapter = new ProductosAdapter(productos, getActivity());
                            rev_productos.setAdapter(mAdapter);
                        } else {
                            Log.e("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });


        return root;
    }
}

class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ViewHolder> {

    private JSONArray productos;

    private Activity miActividad;

    private SharedPreferences mispreferencias;

    private JSONArray favoritos;

    public ProductosAdapter(JSONArray productos, Activity miActividad) {
        this.productos = productos;
        this.miActividad = miActividad;
        this.mispreferencias = miActividad.getSharedPreferences(Constant.PREFERENCE, Context.MODE_PRIVATE);

        try {
            this.favoritos = new JSONArray(mispreferencias.getString("favoritos", "[]"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_productos, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        try {
            Log.e("POS_rec", "POS: " + position);
            String codigo = productos.getJSONObject(position).getString("codigo");
            String nombre = productos.getJSONObject(position).getString("nombre");
            String categoria = productos.getJSONObject(position).getString("categoria");
            String precio = productos.getJSONObject(position).getString("precio");
            String imagen = productos.getJSONObject(position).getString("image");

            holder.tev_item_nombre.setText(nombre);
            holder.tev_item_categoria.setText(categoria);
            holder.tev_item_precio.setText(precio);

            if (esFavorito(codigo)) {
                holder.btn_item_favorito.setImageDrawable(miActividad.getDrawable(R.drawable.ic_favorite));
            } else {
                holder.btn_item_favorito.setImageDrawable(miActividad.getDrawable(R.drawable.ic_no_favorite));
            }

            //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(holder.imv_item_imagen);

            Glide.with(miActividad).load(imagen).into(holder.imv_item_imagen);

            holder.btn_item_vermas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Log.e("PRODUCTO", productos.getJSONObject(position).toString());

                        Intent intent = new Intent(miActividad, DetalleProductoActivity.class);
                        intent.putExtra("producto", productos.getJSONObject(position).toString());
                        miActividad.startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            holder.btn_item_favorito.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {

                        if (esFavorito(codigo)) {
                            holder.btn_item_favorito.setImageDrawable(miActividad.getDrawable(R.drawable.ic_no_favorite));
                            eliminarFavorito(codigo);


                        } else {
                            holder.btn_item_favorito.setImageDrawable(miActividad.getDrawable(R.drawable.ic_favorite));
                            JSONArray favoritos = new JSONArray(mispreferencias.getString("favoritos", "[]"));

                            favoritos.put(productos.getJSONObject(position));

                            SharedPreferences.Editor editor = mispreferencias.edit();
                            editor.putString("favoritos", favoritos.toString());
                            editor.commit();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });


        } catch (JSONException e) {
            holder.tev_item_nombre.setText("error");
        }

    }

    public boolean esFavorito(String codigo) {

        for (int i = 0; i < favoritos.length(); i++) {
            try {
                if (favoritos.getJSONObject(i).getString("codigo").equals(codigo)) {


                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return false;

    }

    public void eliminarFavorito(String codigo) {

        for (int i = 0; i < favoritos.length(); i++) {
            try {
                if (favoritos.getJSONObject(i).getString("codigo").equals(codigo)) {

                    favoritos.remove(i);
                    SharedPreferences.Editor editor = mispreferencias.edit();
                    editor.putString("favoritos", favoritos.toString());
                    editor.commit();
                    return;

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



    }

    @Override
    public int getItemCount() {
//        return userModelList.size();
        return this.productos.length();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tev_item_nombre;
        private TextView tev_item_categoria;
        private TextView tev_item_precio;
        private ImageButton btn_item_favorito;
        private Button btn_item_carrito;
        private ImageView imv_item_imagen;
        private Button btn_item_vermas;
        public ViewHolder(View v) {
            super(v);
            tev_item_nombre = v.findViewById(R.id.tev_item_nombre);
            tev_item_categoria = v.findViewById(R.id.tev_item_categoria);
            tev_item_precio = v.findViewById(R.id.tev_item_precio);
            btn_item_favorito = v.findViewById(R.id.btn_item_favorito);
            btn_item_carrito = v.findViewById(R.id.btn_item_carrito);
            imv_item_imagen = v.findViewById(R.id.imv_item_imagen);
            btn_item_vermas = v.findViewById(R.id.btn_item_vermas);

        }
    }
}
