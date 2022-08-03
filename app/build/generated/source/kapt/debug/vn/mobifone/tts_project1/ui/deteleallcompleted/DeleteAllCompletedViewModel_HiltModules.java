package vn.mobifone.tts_project1.ui.deteleallcompleted;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.android.internal.lifecycle.HiltViewModelMap;
import dagger.hilt.codegen.OriginatingElement;
import dagger.multibindings.IntoMap;
import dagger.multibindings.IntoSet;
import dagger.multibindings.StringKey;
import java.lang.String;

@OriginatingElement(
    topLevelClass = DeleteAllCompletedViewModel.class
)
public final class DeleteAllCompletedViewModel_HiltModules {
  private DeleteAllCompletedViewModel_HiltModules() {
  }

  @Module
  @InstallIn(ViewModelComponent.class)
  public abstract static class BindsModule {
    private BindsModule() {
    }

    @Binds
    @IntoMap
    @StringKey("vn.mobifone.tts_project1.ui.deteleallcompleted.DeleteAllCompletedViewModel")
    @HiltViewModelMap
    public abstract ViewModel binds(DeleteAllCompletedViewModel vm);
  }

  @Module
  @InstallIn(ActivityRetainedComponent.class)
  public static final class KeyModule {
    private KeyModule() {
    }

    @Provides
    @IntoSet
    @HiltViewModelMap.KeySet
    public static String provide() {
      return "vn.mobifone.tts_project1.ui.deteleallcompleted.DeleteAllCompletedViewModel";
    }
  }
}
