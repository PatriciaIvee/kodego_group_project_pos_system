package ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.databinding.ActivityMainBinding
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.ui.orders.OrderListViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var orders: TextView
    private lateinit var orderListViewModel: OrderListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_orders, R.id.nav_sales, R.id.nav_receipts
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        orders = MenuItemCompat.getActionView(navView.getMenu().
        findItem(R.id.nav_orders)) as TextView
        initializeCountDrawer()

        //GET THE ORDER LIST TOTAL ORDERS FROM THE ORDERLIST FRAGMENT
        orderListViewModel = ViewModelProvider(this).get(OrderListViewModel::class.java)
        orderListViewModel.totalOrderQuantity.observe(this) { quantity ->
            if (quantity > 0) {
                orders.text = quantity.toString()
                orders.visibility = View.VISIBLE
            } else if (quantity > 99) {
                orders.text = "${quantity.toString()} +"
                orders.visibility = View.VISIBLE
            } else {
                orders.visibility = View.GONE
            }
        }

    }

    private fun initializeCountDrawer() {
        // Gravity property aligns the text
        orders.gravity = Gravity.CENTER_VERTICAL
        orders.setTypeface(null, Typeface.BOLD)
        orders.setTextColor(resources.getColor(R.color.yellow))
        orders.text = "99+"
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


}