package ru.alexsumin.northwest.math;

import java.util.ArrayList;
import java.util.List;

public class Solver {


    List shopNeeds = new ArrayList();
    List storageStock = new ArrayList();
    List costTable = new ArrayList();
    MathCore core;


    public void setShopNeeds(List shopNeeds) {
        this.shopNeeds = shopNeeds;
    }

    public void setStorageStock(List storageStock) {
        this.storageStock = storageStock;
    }

    public void setCostTable(List costTable) {
        this.costTable = costTable;
    }

    public int[][] calcNW() {
        core = new MathCore(shopNeeds, storageStock, costTable);
        return core.createBasicRoutes();

    }

    public List solveTask(int maxIterations) {
        List result = new ArrayList();
        MathCore core = new MathCore(shopNeeds, storageStock, costTable);

        /*Make our task closed*/
        if (core.makeClosed()) {
            //System.out.println("Транспортная задача закрыта изначально");
        } else {
            //System.out.println("Транспортная задача закрыта в процессе решения");
        }


        /*create first routing map*/
        //System.out.println("Построим начальную карту перевозок:");
//        System.out.println(core.createBasicRoutes());
        core.createBasicRoutes();

        /*calculate current cost*/
        //System.out.println("Текущая стоимость перевозок: " + core.getCurrentCost());
        core.getCurrentCost();



        /*check for degeneracy and fix it*/
//        if (core.checkDegeneracy()) {
//            System.out.println("Задача не вырождена");
//        } else {
//            System.out.println("Присутствующая в задаче вырожденность исправлена");
//        }

        core.checkDegeneracy();

        /*start iterations. maxIterations is need for avoid looping*/
        //System.out.println("Начинаем итерационный процесс:");
        for (int i = 0; i < maxIterations; i++) {
            /*write some decorations*/

            //System.out.println(("              Итерация #" + (i + 1)));


            /*calculate potentials for orders and storages*/
            //System.out.println("Рассчитаем потенциалы:");
            core.checkDegeneracy();
            core.calcPotintials();
            //System.out.println("Для поставшиков:  (" + core.getStockPotentials() + ")");
            core.getStockPotentials();
            core.getOrdersPotentials();


            /*calculate deltas for every cell*/
            core.calcDeltas();

            /*find minimal delta*/
            //System.out.println("Отыщем минимальное Δ: ");
            //System.out.println(core.getMinimalDelta());
            core.getMinimalDelta();





            /*check the iterative process to perfection*/
            if (core.getMinimalDelta() >= 0) {

                //System.out.println("Все значения Δ неотрицательны\nОптимизация завершена!");
                //System.out.println("Текущая стоимость: " + core.getCurrentCost());
                result.add(core.getCurrentCost());
                result.add(core.getCurrentRoutes());
                result.add(true);
                return result;
            }
            /*make a cyclical route on basic cells*/
            //System.out.println("Построим замкнутый цикл:");
            List<int[]> cycle = core.getCycle(core.getMinimalDeltaCoords());
            //System.out.println(writePath(cycle));

            /*redistribute routing*/
            //System.out.println("Перераспределим перевозки и просчитаем стоимость: ");
            core.redistribute();

            /*calculate current cost*/
            //System.out.println("" + core.getCurrentCost());
            core.getCurrentCost();


        }

        /*return our data as-is. Maybe something is wrong?*/
        result.add(core.getCurrentCost());
        result.add(core.getCurrentRoutes());
        result.add(false);
        return result;
    }

}
