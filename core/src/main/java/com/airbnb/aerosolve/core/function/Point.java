package com.airbnb.aerosolve.core.function;

import com.airbnb.aerosolve.core.FunctionForm;
import com.airbnb.aerosolve.core.ModelRecord;

import java.util.ArrayList;
import java.util.List;

public class Point implements Function {
  private float weight;

  public Point(float weight) {
    this.weight = weight;
  }

  public Point(ModelRecord record) {
    List<Double> weightVec = record.getWeightVector();
    weight = weightVec.get(0).floatValue();
  }

  @Override
  public Function aggregate(Iterable<Function> functions, float scale, int numBins) {
    float aggWeight = 0;
    for (Function fun: functions) {
      Point point = (Point) fun;
      aggWeight += scale * point.weight;
    }
    return new Point(aggWeight);
  }

  /*
    for string feature, aerosolve assume x[0] == 1
   */
  @Override
  public float evaluate(float... x) {
    return weight * x[0];
  }

  @Override
  public float evaluate(List<Double> values) {
    return weight * values.get(0).floatValue();
  }

  @Override
  public void update(float delta, float... values) {
    weight += delta * values[0];
  }

  @Override
  public void update(float delta, List<Double> values) {
    weight += delta * values.get(0);
  }

  @Override
  public ModelRecord toModelRecord(String featureFamily, String featureName) {
    ModelRecord record = new ModelRecord();
    record.setFunctionForm(FunctionForm.Point);
    record.setFeatureFamily(featureFamily);
    record.setFeatureName(featureName);
    ArrayList<Double> arrayList = new ArrayList<Double>();
    arrayList.add((double)weight);
    record.setWeightVector(arrayList);
    return record;
  }

  @Override
  public void setPriors(float[] params) {
    weight = params[0];
  }

  @Override
  public void LInfinityCap(float cap) {
    if (cap <= 0.0f) return;
    weight = Math.min(cap, weight);
  }

  @Override
  public float LInfinityNorm() {
    return weight;
  }

  @Override
  public void resample(int newBins) {

  }

  @Override
  public void smooth(double tolerance) {

  }

  @Override
  public Function clone() throws CloneNotSupportedException {
    return new Point(weight);
  }
}
