package com.ffcs.miaucats.ui.main

import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.ffcs.miaucats.R
import com.ffcs.miaucats.constants.Constants
import com.ffcs.miaucats.databinding.ActivityMainBinding
import com.ffcs.miaucats.model.Imagem

class MainActivity : AppCompatActivity(),
MainImageAdapter.OnImagemClickListener{
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel
    private lateinit var fotosAdapter: MainImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        //Observers
        viewModel.imagens.observe(this, { imagens ->
            imagens?.let {
                setupRecyclerView(imagens)
            }
        })

        viewModel.error.observe(this, { isError ->
            if (isError) {
                getErroRequest()
            }
        })

        setupActivity()
        getImagens()
    }

    private fun setupActivity() {
        fotosAdapter = MainImageAdapter(this@MainActivity,
            arrayListOf(),
            this@MainActivity)

        binding.rvImagens.apply {
            layoutManager = GridLayoutManager(this@MainActivity, Constants.SPAN_COUNT)
            adapter = fotosAdapter
        }
    }

    private fun getImagens() {
        viewModel.fetchImagens()
    }

    private fun setupRecyclerView(imagens: List<Imagem>) {
        fotosAdapter = MainImageAdapter(
            this@MainActivity,
            imagens as ArrayList<Imagem>,
            this@MainActivity
        )

        binding.rvImagens.apply {
            layoutManager = GridLayoutManager(this@MainActivity, Constants.SPAN_COUNT)
            adapter = fotosAdapter
        }
    }

    private fun getErroRequest() {
        fotosAdapter.updateAdapter(arrayListOf())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onImagemClick(description: String?) {
        if(description != null && description != "null"){
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.apply {
                setTitle(getString(R.string.alert_titulo_descricao))
                setMessage(description)
            }.create().show()
        }else{
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.apply {
                setTitle(getString(R.string.alert_titulo_descricao))
                setMessage(getString(R.string.alert_mensagem_no_descricao))
            }.create().show()
        }
    }
}