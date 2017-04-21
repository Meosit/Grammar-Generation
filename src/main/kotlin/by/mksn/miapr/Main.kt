package by.mksn.miapr

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage


class Main : Application() {

    override fun start(primaryStage: Stage) {
        val root = FXMLLoader.load<Parent>(Thread.currentThread().contextClassLoader.getResource("Window.fxml"))
        primaryStage.title = "Grammar generation"
        primaryStage.scene = Scene(root, 800.0, 650.0)
        primaryStage.show()
    }


    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            launch(Main::class.java)
        }

    }


}

