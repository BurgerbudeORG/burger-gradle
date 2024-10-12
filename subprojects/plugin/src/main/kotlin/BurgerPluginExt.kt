import org.burgerbude.gradle.plugin.settings.BurgerSettings
import org.gradle.api.Action
import org.gradle.api.initialization.Settings

fun Settings.burgerSettings(action: Action<BurgerSettings>) {
  extensions.findByType(BurgerSettings::class.java)?.apply {
    action.execute(this)
  }
}