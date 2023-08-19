
# from sklearn.svm import SVC
from sklearn.ensemble import AdaBoostClassifier
from xgboost import XGBClassifier
from sklearn.metrics import classification_report
import pickle


def run_model_training(X_train, X_test, y_train, y_test):
    xgb = AdaBoostClassifier()
    xgb.fit(X_train, y_train)
    y_pred = xgb.predict(X_test)

    print("Training Accuracy :", xgb.score(X_train, y_train))

    print("Testing Accuracy :", xgb.score(X_test, y_test))

    print("\n3. Model Performance: \n")
    print(classification_report(y_test, y_pred))

    return xgb
