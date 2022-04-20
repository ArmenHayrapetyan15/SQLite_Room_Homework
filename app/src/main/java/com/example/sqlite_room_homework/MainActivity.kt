package com.example.sqlite_room_homework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import com.example.sqlite_room_homework.databinding.ActivityMainBinding
import com.example.sqlite_room_homework.db.UserEntity


class MainActivity : AppCompatActivity(), RecyclerViewAdapter.RowClickListener {
    lateinit var binding: ActivityMainBinding
    lateinit var recyclerViewAdapter: RecyclerViewAdapter
    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerViewAdapter = RecyclerViewAdapter(this@MainActivity)
            adapter = recyclerViewAdapter
            val divider = DividerItemDecoration(applicationContext, VERTICAL)
            addItemDecoration(divider)
        }

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        viewModel.getAllUsersObservers().observe(this, Observer {
            recyclerViewAdapter.setListData(ArrayList(it))
            recyclerViewAdapter.notifyDataSetChanged()
        })

        binding.saveButton.setOnClickListener {
            val name = binding.nameInput.text.toString()
            val email = binding.emailInput.text.toString()

            if (name == "" || email == "") {
                Toast.makeText(this, "You did not enter your name or email", Toast.LENGTH_SHORT)
                    .show()
            } else if (binding.saveButton.text.equals("Save")) {
                val user = UserEntity(0, name, email)
                viewModel.insertUserInfo(user)
            } else {
                val user = UserEntity(
                    binding.nameInput.getTag(binding.nameInput.id).toString().toInt(),
                    name,
                    email
                )
                viewModel.updateUserInfo(user)
                binding.saveButton.setText("Save")
            }
            binding.nameInput.setText("")
            binding.emailInput.setText("")
        }
    }

    override fun onDeleteUserClickListener(user: UserEntity) {
        viewModel.deleteUserInfo(user)
    }

    override fun onItemClickListener(user: UserEntity) {
        binding.nameInput.setText(user.name)
        binding.emailInput.setText(user.email)
        binding.nameInput.setTag(binding.nameInput.id, user.id)
        binding.saveButton.setText("Update")
    }
}