package com.example.demo.models;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static java.lang.Thread.sleep;

@Service
public class Diagnostic {
    public TreeMap<Integer, ArrayList<ArrayList<Integer>>> rIJ = new TreeMap<>();
    public List<ArrayList<Integer>> test = new ArrayList<>();
    public List<ArrayList<Integer>> records = new ArrayList<>();
    public int [][] matrix = new int [21][21];

    public ArrayList<ArrayList<Integer>> work() throws InterruptedException {
        records = getMatrix("src/main/resources/static/graph.csv");
        test = getMatrix("src/main/resources/static/test3.csv");
        return walk2();
    }

    public List<ArrayList<Integer>> getMatrix(String nameFile){
        List<ArrayList<Integer>> matrix = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(nameFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                ArrayList<Integer> val = new ArrayList<>();
                for(String str: values){
                    val.add(Integer.parseInt(str));
                }
                matrix.add(val);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matrix;
    }

    public ArrayList<ArrayList<Integer>> walk2() throws InterruptedException {
        int[] rResult;
        ArrayList<ArrayList<Integer>> resultDiagnose = new ArrayList<>();
        do {
            rIJ = new TreeMap<>();
            matrix = drawMatrix();
            int idNode = (int) (Math.random() * 21); // обираємо початковий вузол
            int lastNode = 5;
            rec(idNode, lastNode);
            rResult = sumR();
            if(rResult[0] == 0) {
                System.out.println("Помилки не виявлено");
            }
            if(rResult[0] == 1){
                System.out.println("Виявлена помилка");
                resultDiagnose = oneErr(rResult[1]);
                System.out.println(resultDiagnose);
            }
            if(rResult[0] > 1){
                resultDiagnose = multyErr(rResult[1]);
                System.out.println(resultDiagnose);
            }
            if (rResult[0] > 0){
                sleep(3200);
            }
            if(resultDiagnose == null){
                continue;
            }
        }while (resultDiagnose.size() == 0);

        return resultDiagnose;
    }


    public void rec(int indx, int lastNode){
        ArrayList<Integer> line = (ArrayList<Integer>) records.get(indx); // обираємо рядок з шляхами з поточного вузла
        for(int i = 0; i < 21; i++){
            if(line.get(i) == 1){
                matrix[indx][i] = 1;
                testing(test, i, indx);
                lastNode--;
                if(lastNode > 0) {
                    rec(i, lastNode);
                }
                else {
                    return;
                }
            }
        }
    }

    //Обрахунок фактичного синдрому
    public int[] sumR(){
        int R = 0;//ініціалізуємо змінну для запису синдрому
        ArrayList<ArrayList<Integer>> maxRes;//ініціалізуємо змінну яка міститиме накопичену інформацію з вузла
        int max = 0;
        int idMax = 0;
        //пошук вузла що накопив найбільше діагностичної інформації
        for(Integer key: rIJ.keySet()){
            if(max < rIJ.get(key).size()){
                max = rIJ.get(key).size();
                idMax = key;
            }
            System.out.println(max);
        }
        maxRes = rIJ.get(idMax);
        //обчислення синдрому
        for(ArrayList<Integer> arr: maxRes){
            R += arr.get(2);
        }

        return new int[] {R, idMax};
    }
    //Тестування двох вузлів
    public void testing(List<ArrayList<Integer>> test, int nextNode, int idNode){
        int res = test.get(nextNode).get(idNode); //отримуємо результат тестування
        ArrayList<Integer> testInfo = new ArrayList<>();//створюємо масив, в який буде записано інформацю про поточний тест
        testInfo.add(idNode);//додаємо номер вузла, який тестували
        testInfo.add(nextNode);//додаємо номер вузла, що тестував
        testInfo.add(res);// додаємо результат тестування
        if(res == 0){//у випадку задовільного результату додаємо інформацію на вузол який тестував
            ArrayList<ArrayList<Integer>> list;
            if(rIJ.containsKey(idNode)){
                list = rIJ.get(idNode);
            }else {
                list = new ArrayList<>();
            }
            if(!list.contains(testInfo)) {
                list.add(testInfo);
            }
            System.out.println(list);
            rIJ.put(nextNode, list);

        }else {//у випадку негативного результату інформацію залишаємо на вузлі який тестували
            ArrayList<ArrayList<Integer>> list;
            if(rIJ.containsKey(idNode)){
                list = rIJ.get(idNode);
            }else {
                list = new ArrayList<>();
            }
            if(!list.contains(testInfo)) {
                list.add(testInfo);
            }
            rIJ.put(idNode, list);
        }
    }
    //Пошук відмови вузла при R = 1
    public ArrayList<ArrayList<Integer>> oneErr(int idNode){
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        ArrayList<ArrayList<Integer>> listTestValues = rIJ.get(idNode);
        int idTestNode = -1;
        int idErrNode = -1;
        for (ArrayList<Integer> value : listTestValues) {
            if (value.get(2) == 1) {
                idErrNode = value.get(0);
                idTestNode = value.get(1);
            }
        }

        //обчислення локальних степенів вхідних ребер
        int aS = 0;
        int aT = 0;
        for(Integer val: matrix[idErrNode]){
            aS += val;
        }
        for(Integer val: matrix[idTestNode]){
            aT += val;
        }

        for (ArrayList<Integer> value : listTestValues) {
            if (value.get(0) == idTestNode && value.get(1) == idErrNode) {
                if (test.get(value.get(idErrNode)).get(value.get(idNode)) == 0) {
                    //на вузлі, що тестував відбувся збій

                    if (aT > 1) {
                        ArrayList<Integer> node = new ArrayList<>();
                        node.add(idTestNode + 1);
                        node.add(1);
                        result.add(node);
                        System.out.println("на вузлі " + (idTestNode + 1) + " відбувся збій");
                    } else {
                        ArrayList<Integer> node = new ArrayList<>();
                        node.add(idTestNode + 1);
                        node.add(2);
                        result.add(node);
                        System.out.println("вузол " + (idTestNode + 1) + "не працює");
                    }
                    return result;
                }
            }
        }
        if (aS >= 1) {
            if (aT > 1) {
                ArrayList<Integer> node = new ArrayList<>();
                node.add(idTestNode + 1);
                node.add(1);
                result.add(node);
                ArrayList<Integer> node2 = new ArrayList<>();

                node2.add(idErrNode + 1);
                node2.add(1);
                result.add(node2);
                System.out.println("у вузлі " + (idErrNode + 1) + " чи " + (idTestNode + 1) + " відбувся збій");
            } else {
                ArrayList<Integer> node = new ArrayList<>();
                node.add(idTestNode + 1);
                node.add(2);
                result.add(node);
                //System.out.println("вузол " + (idTestNode + 1) + " не працює");
            }
            return result;
        }
        if (aT > 1) {
            ArrayList<Integer> node = new ArrayList<>();
            node.add(idErrNode + 1);
            node.add(2);
            result.add(node);
            //System.out.println("вузол " + (idErrNode + 1) + " не працює");
            return result;
        } else {
            System.out.println("Недостатньо діагностичної інформації");
            return null;

        }

    }

    public ArrayList<ArrayList<Integer>> multyErr(int idNode) {
        ArrayList<ArrayList<Integer>> finalResult = new ArrayList<>();
        System.out.println("Збій чи помилка в декількох вузлах");

        //видалення ребер з результатом - 1
        ArrayList<ArrayList<Integer>> testData = rIJ.get(idNode);

        for (ArrayList<Integer> val : testData) {
            if (val.get(2) == 1) {
                matrix[val.get(0)][val.get(1)] = 0;
            }
        }

        //перетворення на неорієнтований граф
        for (int i = 0; i < 21; i++) {
            for (int j = 0; j < 21; j++) {
                if (matrix[i][j] == 1) {
                    matrix[j][i] = 1;
                }
            }
        }
        //перевірка на те чи зв'язний граф
        ArrayList<Integer> H = new ArrayList<>();//список вершин графа

        TreeMap<Integer, ArrayList<Integer>> xM = new TreeMap<>();

        for (int i = 0; i < 21; i++) {
            for (int j = 0; j < 21; j++) {
                if (matrix[i][j] == 1) {
                    H.add(i);
                    break;
                }
            }
        }
        System.out.println(H);


        int l = 0;
        while (H.size() != 0) {
            l++;
            System.out.println("x" + l);
            //випадково обираємо вершину
            int indx = (int) (Math.random() * H.size());
            ArrayList<Integer> nodes = new ArrayList<>();
            int tmplNode = H.get(indx);
            nodes.add(H.get(indx));
            System.out.println((tmplNode) + "->(");
            //шукаємо вершини з якими зв'язана поточна
            for (int i = 0; i < 21; i++) {
                if (matrix[tmplNode][i] == 1) {
                    //System.out.println((i+1)+"->");
                    matrix[tmplNode][i] = 0;
                    //зливаємо вершини
                    for (int k = 0; k < 21; k++) {
                        if (k == tmplNode) {
                            matrix[i][k] = 0;
                        }
                        if (matrix[i][k] == 1) {
                            matrix[tmplNode][k] = 1;
                        }
                    }

                    for (int k = 0; k < 21; k++) {
                        if (matrix[k][i] == 1) {
                            matrix[k][i] = 0;
                            matrix[k][tmplNode] = 1;
                        }
                    }
                    nodes.add(i);
                    System.out.println(H);

                    H.remove(H.indexOf(i));
                    matrix[tmplNode][i] = 0;
                    i = -1;

                }
                if (H.size() == 1) {
                    break;
                }
            }
            //створюємо підмножину Хn
            xM.put(l, nodes);
            System.out.println("nodes " + nodes);

            System.out.println(xM.values());

            //видаляємо поточну вершину з графу
            System.out.println("---");
            System.out.println(H.indexOf(tmplNode));
            System.out.println(H);
            H.remove(H.indexOf(tmplNode));
            System.out.println("l = " + l);
        }
        if (l == 1) {
            System.out.println("граф зв'язний");
            ArrayList<Integer> nodes = xM.get(1);
            System.out.println(nodes);
            System.out.println(rIJ.get(idNode));
            ArrayList<ArrayList<Integer>> testInfo = rIJ.get(idNode);
            int k = 0;
            while (k < nodes.size()) {
                k++;
                rIJ = new TreeMap<>();
                matrix = drawMatrix();
                rec(idNode, 7);
            }
            ArrayList<Integer> sums = new ArrayList<>();
            int count = 0;
            for(int i = 0; i < testInfo.size(); i++) {
                int sum = 0;
                for (Integer key : rIJ.keySet()) {
                    for(ArrayList<Integer> value : rIJ.get(key)){
                        if(value.get(0).equals(testInfo.get(i).get(0)) && value.get(1).equals(testInfo.get(i).get(1))){
                            count++;
                            sum = value.get(2);
                        }
                    }
                }
                sums.add(sum);
            }
            for(int i = 0; i < testInfo.size(); i++){
                ArrayList<Integer> resInfo = new ArrayList<>();
                if(testInfo.get(i).get(2) == 1){
                    if (sums.get(i) != count){
                        resInfo.add(testInfo.get(i).get(0) + 1);
                        resInfo.add(2);
                        finalResult.add(resInfo);
                        System.out.println("У вузлі " + testInfo.get(i).get(0) + " збій");
                    }else {
                        System.out.println("Вузол " + testInfo.get(i).get(0) + " непрацює");
                    }
                }
                if(testInfo.get(i).get(2) == 0){
                    if (sums.get(i) != 0){
                        resInfo.add(testInfo.get(i).get(0) + 1);
                        resInfo.add(1);
                        finalResult.add(resInfo);
                        System.out.println("У вузлі " + testInfo.get(i).get(0) + " збій");
                    }else {
                        System.out.println("Вузол " + testInfo.get(i).get(0) + " працює");
                    }
                }
            }
        } else {
            System.out.println("граф не зв'язний");
            ArrayList<Double> pi = new ArrayList<>();
            ArrayList<Integer> countTest = new ArrayList<>();
            double pr = 0.5;
            for (Integer key : xM.keySet()) {
                ArrayList<Integer> currentNodes = xM.get(key);
                int n = currentNodes.size();
                int S = 0;
                double pAS;
                int k = 0;
                int sum = 0;
                int M = 0;
                double pAH = 0;
                double pHk = 1;
                double p = 0.8;
                double q = 0.2;
                ArrayList<Double> listPHk = new ArrayList<>();
                ArrayList<Double> listPAH = new ArrayList<>();
                ArrayList<Integer> HS = new ArrayList<>();
                for (Integer currentNode : currentNodes) {
                    for (Integer node : currentNodes) {
                        if (records.get(currentNode).get(node) == 1) {
                            sum += test.get(node).get(currentNode);
                            M++;

                        }
                    }
                }
                countTest.add(M);

                if (sum == 0) {
                    pAS = 1.0;
                    pi.add(pAS);
                } else {
                    String str = Integer.toBinaryString(S);
                    int m = 0;
                    pAS = 1.0;
                    while (str.equals("1".repeat(n))) {
                        S++;
                        str = Integer.toBinaryString(S);
                        boolean step = true;
                        m++;
                        while (m <= M){
                            for (int i = 0; i < n; i++) {
                                for (int j = 0; j < n; j++) {
                                    if (records.get(currentNodes.get(i)).get(currentNodes.get(j)) == 1) {
                                        int r = test.get(currentNodes.get(j)).get(currentNodes.get(j));
                                        if (str.charAt(i) == '1') {
                                            pAS *= pr;
                                            m++;
                                        } else {
                                            if (str.charAt(j) == '0') {
                                                if (r == 0) {
                                                    pAS *= 1;
                                                    m++;
                                                } else {
                                                    step = false;
                                                    break;
                                                }
                                            } else {
                                                if (r == 0) {
                                                    step = false;
                                                    break;
                                                } else {
                                                    pAS *= 1;
                                                    m++;
                                                }
                                            }
                                        }

                                    }
                                }
                                if(!step){
                                    break;
                                }
                            }
                            if(!step){
                                break;
                            }
                        }
                        if(step) {
                            k++;
                            HS.add(S);
                            pAH = pAS;
                            listPAH.add(pAH);
                            if(str.length() != n){
                                str = "0".repeat(n - str.length()) + str;
                            }
                            for(int i = 0; i < n; i++){
                                int sn = str.charAt(i);
                                pHk = pHk * Math.pow(p,(sn + 1) % 2) * Math.pow(q, sn);
                            }
                            listPHk.add(pHk);
                        }
                    }
                    double pA = 0;
                    for(int i = 0; i < k; i++){
                        pA = pA + listPHk.get(i) * listPAH.get(i);
                    }
                    ArrayList<Double> listPHa = new ArrayList<>();
                    for(int i = 0; i < k; k++){
                        listPHa.add(listPHk.get(i) * listPAH.get(i) / pA);
                    }
                    double curPi = 0.0;
                    for(int i = 0; i < n; i++){
                        for(int j = 0; j < k; j++){
                            String s = Integer.toBinaryString(HS.get(k));
                            if(s.length() < n){
                                s = "0".repeat(n - s.length()) + str;
                            }
                            if(s.charAt(i) == '0'){
                                curPi += listPHa.get(j);
                            }
                        }
                    }
                    pi.add(curPi);
                }
            }
            ArrayList<Integer> max = new ArrayList<>();
            for(int i = 0; i < pi.size(); i++){
                if(pi.get(i) > 0.95 * Math.pow(0.5, countTest.get(i))){
                    max.add(i+1);
                };
            }
            ArrayList<Integer> errNodes = new ArrayList<>();
            ArrayList<Integer> workNode = new ArrayList<>();
            for (Integer key : xM.keySet()){
                if (!max.contains(key)) {
                    errNodes.addAll(xM.get(key));
                }
                else{
                    workNode.addAll(xM.get(key));
                }
            }

            System.out.println("Несправні вузли " + errNodes);
            for(int i = 0; i < errNodes.size(); i++){
                ArrayList<Integer> resInfo = new ArrayList<>();
                resInfo.add(errNodes.get(i) + 1);
                resInfo.add(2);
                finalResult.add(resInfo);
            }
            System.out.println("Робочі вузли " + workNode);

        }
        System.out.println(finalResult);
        return finalResult;
    }

    public int[][] drawMatrix (){
        int [][] result = new int [21][21];
        for(int i = 0; i < 21; i++){
            for(int j = 0; j < 21; j++){
                result[i][j] = 0;
            }
        }
        return result;
    }

}
