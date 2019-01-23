//AndrewID: yuhaozhu Name: Yuhao Zhu

package hw2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

public class NutriByte extends Application {
	static Model model = new Model(); // made static to make accessible in the controller
	static View view = new View(); // made static to make accessible in the controller
	static Person person; // made static to make accessible in the controller

	Controller controller = new Controller(); // all event handlers

	/**
	 * Uncomment the following three lines if you want to try out the full-size data
	 * files
	 */
//	static final String PRODUCT_FILE = "data/Products.csv";
//	static final String NUTRIENT_FILE = "data/Nutrients.csv";
//	static final String SERVING_SIZE_FILE = "data/ServingSize.csv";

	/**
	 * The following constants refer to the data files to be used for this
	 * application
	 */
//	static final String PRODUCT_FILE = "data/Nutri2Products.csv";
//	static final String NUTRIENT_FILE = "data/Nutri2Nutrients.csv";
//	static final String SERVING_SIZE_FILE = "data/Nutri2ServingSize.csv";

	static final String PRODUCT_FILE = "data/SampleProducts.csv";
	static final String NUTRIENT_FILE = "data/SampleNutrients.csv";
	static final String SERVING_SIZE_FILE = "data/SampleServingSize.csv";

	static final String NUTRIBYTE_IMAGE_FILE = "NutriByteLogo.png"; // Refers to the file holding NutriByte logo image

	static final String NUTRIBYTE_PROFILE_PATH = "profiles"; // folder that has profile data files
	static final String PROFILE_CSV_FILE = "profiles/Profile1.csv";

	static final int NUTRIBYTE_SCREEN_WIDTH = 1015;
	static final int NUTRIBYTE_SCREEN_HEIGHT = 675;

	@Override
	public void start(Stage stage) throws Exception {
		model.readProducts(PRODUCT_FILE);
		model.readNutrients(NUTRIENT_FILE);
		model.readServingSizes(SERVING_SIZE_FILE);
		view.setupMenus();
		view.setupNutriTrackerGrid();
		view.root.setCenter(view.setupWelcomeScene());
		Background b = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
		view.root.setBackground(b);
		Scene scene = new Scene(view.root, NUTRIBYTE_SCREEN_WIDTH, NUTRIBYTE_SCREEN_HEIGHT);
		view.root.requestFocus(); // this keeps focus on entire window and allows the textfield-prompt to be
									// visible
		setupBindings();
		stage.setTitle("NutriByte 3.0");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	void setupBindings() {
		view.recommendedNutrientsTableView.setItems(NutriProfiler.recommendedNutrientsList);

		view.newNutriProfileMenuItem.setOnAction(controller.new NewMenuItemHandler());
		view.openNutriProfileMenuItem.setOnAction(controller.new OpenMenuItemHandler());
		view.exitNutriProfileMenuItem.setOnAction(event -> Platform.exit());
		view.aboutMenuItem.setOnAction(controller.new AboutMenuItemHandler());

		view.recommendedNutrientNameColumn.setCellValueFactory(recommendedNutrientNameCallback);
		view.recommendedNutrientQuantityColumn.setCellValueFactory(recommendedNutrientQuantityCallback);
		view.recommendedNutrientUomColumn.setCellValueFactory(recommendedNutrientUomCallback);

		view.createProfileButton.setOnAction(controller.new RecommendNutrientsButtonHandler());
	}

	Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>> recommendedNutrientNameCallback = new Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<RecommendedNutrient, String> arg0) {
			Nutrient nutrient = Model.nutrientsMap.get(arg0.getValue().getNutrientCode());
			return nutrient.nutrientNameProperty();
		}
	};

	Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>> recommendedNutrientQuantityCallback = new Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<RecommendedNutrient, String> arg0) {
			Nutrient nutrient = Model.nutrientsMap.get(arg0.getValue().getNutrientCode());
			for (RecommendedNutrient recommendedNutrient : NutriProfiler.recommendedNutrientsList) {
				if (recommendedNutrient.getNutrientCode().equals(nutrient.getNutrientCode())) {
					return Bindings.format("%.2f", recommendedNutrient.NutrientQuantityProperty());
				}
			}
			return null;
		}
	};

	Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>> recommendedNutrientUomCallback = new Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<RecommendedNutrient, String> arg0) {
			Nutrient nutrient = Model.nutrientsMap.get(arg0.getValue().getNutrientCode());
			return nutrient.nutrientUomProperty();
		}
	};
}
