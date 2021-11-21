package com.example.tiendaonline.ui.gallery;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tiendaonline.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GalleryFragment extends Fragment {

    private Spinner spn_categorias;
    private TextView tev_gallery;
    private RatingBar rab_star;
    private RecyclerView rev_productos;
    private RecyclerView.Adapter mAdapter;


    //creadas manualmente
    private String[] categorias = new String[]{"jean dama", "jean caballero", "blusas", "camisetas", "camisa dama", "camisa caballero"};

    //creadas medainate JSON
    private String productos = "[{\"nombre\":\"jean\",\"categoria\":\"lady\",\"precio\":130000,\"enstock\":true,\"image\":\"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSPgzK_zwiXX2HrZ03hUouF841ZZCMoWcxFQQ&usqp=CAU\"},{\"nombre\":\"shirt\",\"categoria\":\"lady\",\"precio\":30000,\"enstock\":true,\"image\":\"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQVZCeQtLsYikmlkI4eIYdGk3olU493gOR1iQ&usqp=CAU\"},{\"nombre\":\"shirt\",\"categoria\":\"gentleman\",\"precio\":30000,\"enstock\":true,\"image\":\"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT4qPl476SiEu2Aw9HrMNbOL2oAo_cnA2u1aA&usqp=CAU\"},{\"nombre\":\"blouses\",\"categoria\":\"lady\",\"precio\":45000,\"enstock\":true,\"image\":\"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSsBBEiGfxIDNSPOpYxJw2XoKbBAgpdKN3ajA&usqp=CAU\"},{\"nombre\":\"Tshirts\",\"categoria\":\"lady\",\"precio\":50000,\"enstock\":true,\"image\":\"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTocLvNEiu50nFm4vPfB9Gepzb8hqCbRg65lQ&usqp=CAU\"}]";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        tev_gallery = root.findViewById(R.id.tev_gallery);
        spn_categorias = root.findViewById(R.id.spn_categorias);

        rev_productos = root.findViewById(R.id.rev_productos);

        tev_gallery.setText("ESTAS EN LA GALERIA");

        //opcion 1 : array de java
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, categorias);

        //opcion 2 . string array de XML de recursos

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Categorias, android.R.layout.simple_dropdown_item_1line);
        spn_categorias.setAdapter(adapter);

        spn_categorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //tev_gallery.setText(categorias[position]);

                String categoria = spn_categorias.getSelectedItem().toString();
                tev_gallery.setText(categoria);
                Toast.makeText(getActivity(), "Option selected " + categoria, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        //alinear productos en pantalla
        rev_productos.setLayoutManager(new LinearLayoutManager(getActivity()));

        //rev_productos.setLayoutManager(new GridLayoutManager(getActivity(), 2));




        try {
            JSONArray jsonProductos = new JSONArray(productos);
            mAdapter= new ProductosAdapter(jsonProductos, getActivity());
            rev_productos.setAdapter(mAdapter);

            JSONObject producto0 = jsonProductos.getJSONObject(0);

            String nombre = producto0.getString("nombre");

            JSONArray marcas = producto0.getJSONArray("marcas");



        } catch (JSONException e) {
            e.printStackTrace();
        }


        return root;
    }


    }

class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ViewHolder> {

    private JSONArray productos;

    private Activity miActividad;

    public ProductosAdapter(JSONArray productos, Activity miActividad) {
        this.productos = productos;
        this.miActividad = miActividad;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_productos, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        try {
            Log.e("POS_rec", "POS: " + position);
            String nombre = productos.getJSONObject(position).getString("nombre");
            String categoria = productos.getJSONObject(position).getString("categoria");
            String precio = productos.getJSONObject(position).getString("precio");
            String image = productos.getJSONObject(position).getString("image");

            holder.tev_item_nombre.setText(nombre);
            holder.tev_item_categoria.setText(categoria);
            holder.tev_item_precio.setText(precio);

            Glide.with(miActividad).load(image)
                    .placeholder(new ColorDrawable(Color.BLACK))
                    .error(new ColorDrawable(Color.BLUE))
                    .into(holder.imv_item_imagen);


        } catch (JSONException e) {
            holder.tev_item_nombre.setText("error");
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
        private Button btn_item_favorito;
        private Button btn_item_carrito;
        private ImageView imv_item_imagen;

        public ViewHolder(View v) {
            super(v);
            tev_item_nombre = v.findViewById(R.id.tev_item_nombre);
            tev_item_categoria = v.findViewById(R.id.tev_item_categoria);
            tev_item_precio = v.findViewById(R.id.tev_item_precio);
            btn_item_favorito = v.findViewById(R.id.btn_item_favorito);
            btn_item_carrito = v.findViewById(R.id.btn_item_carrito);
            imv_item_imagen = v.findViewById(R.id.imv_item_imagen);

        }
    }
}

//linea para cargar imagenes con link
/* Glide.with(this).load("http://goo.gl/gEgYUd").into(imageView);
        }

        mas herramientas

        @Override public View getView(int position, View recycled, ViewGroup container) {
  final ImageView myImageView;
  if (recycled == null) {
    myImageView = (ImageView) inflater.inflate(R.layout.my_image_view, container, false);
  } else {
    myImageView = (ImageView) recycled;
  }

  String url = myUrls.get(position);

  Glide
    .with(myFragment)
    .load(url)
    .centerCrop()
    .placeholder(R.drawable.loading_spinner)
    .into(myImageView);

  return myImageView;
}
 */