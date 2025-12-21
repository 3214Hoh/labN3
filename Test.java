import java.io.PrintStream;
import java.util.Scanner;

public class Test {
    public static Scanner in = new Scanner(System.in);
    public static PrintStream out = System.out;

    public static void main(String[] args) {
        out.println("1. ТЕСТ СОЗДАНИЯ ГРАФА И ДОБАВЛЕНИЯ РЕБЕР:");
        Graph graph1 = new Graph();
        graph1.add(1, 2);
        graph1.add(2, 3);
        graph1.add(3, 4);
        graph1.add(4, 1);
        graph1.add(1, 3);
        out.println("Граф 1:");
        out.println(graph1);

        out.println("\n2. ТЕСТ НА ДУБЛИКАТЫ:");
        out.println("Пытаемся добавить существующее ребро 1 -> 2:");
        graph1.add(1, 2);
        out.println("Граф после попытки добавления дубликата:");
        out.println(graph1);

        out.println("\n3. ВЫВОД ВЕРШИН ПО УБЫВАНИЮ:");
        out.println("Вершины графа 1: " + graph1.printVertexes());

        out.println("\n4. ВЕРШИНЫ С ВХОДЯЩИМИ РЕБРАМИ > 1:");
        out.println("Результат: " + graph1.VertexesMoreN(1));

        out.println("\nВЕРШИНЫ С ВХОДЯЩИМИ РЕБРАМИ > 0:");
        out.println("Результат: " + graph1.VertexesMoreN(0));

        out.println("\n5. УДАЛЕНИЕ РЕБРА:");
        out.println("Удаляем ребро 2 -> 3:");
        boolean removed = graph1.deleteReb(2, 3);
        out.println("Удалено: " + removed);
        out.println("Граф после удаления:");
        out.println(graph1);

        out.println("\n6. УДАЛЕНИЕ ВЕРШИНЫ:");
        out.println("Удаляем вершину 3:");
        boolean vertexRemoved = graph1.deleteVer(3);
        out.println("Удалено: " + vertexRemoved);
        out.println("Граф после удаления вершины 3:");
        out.println(graph1);

        out.println("\n7. УДАЛЕНИЕ ВЕРШИНЫ С НАИБОЛЬШИМ КОЛИЧЕСТВОМ ВХОДЯЩИХ РЕБЕР:");
        Graph graph2 = new Graph();
        graph2.add(1, 2);
        graph2.add(1, 3);
        graph2.add(2, 3);
        graph2.add(4, 3);
        graph2.add(5, 3);
        out.println("Граф 2 до удаления:");
        out.println(graph2);

        boolean minRemoved = graph2.deleteMinVer();
        out.println("Вершина удалена: " + minRemoved);
        out.println("Граф после удаления вершины с max входящими ребрами:");
        out.println(graph2);

        out.println("\n8. КОМПОНЕНТЫ СВЯЗНОСТИ:");

        Graph graph3 = new Graph();
        graph3.add(1, 2);
        graph3.add(2, 3);
        graph3.add(3, 1);
        graph3.add(4, 5);
        graph3.add(5, 4);
        graph3.add(6, 6);

        out.println("Граф 3:");
        out.println(graph3);
        out.println("Компоненты связности: " + graph3.searchComp());

        out.println("\n9. ДОСТИЖИМЫЕ ВЕРШИНЫ ЗА 2 ХОДА:");
        Graph graph4 = new Graph();
        graph4.add(1, 2);
        graph4.add(2, 3);
        graph4.add(3, 4);
        graph4.add(2, 5);
        graph4.add(5, 6);

        out.println("Граф 4:");
        out.println(graph4);
        out.println("Из вершины 1 за 2 хода можно достичь: " + graph4.motionVer2(1));

        out.println("\n10. ДОСТИЖИМЫЕ ВЕРШИНЫ ЗА 3 ХОДА:");
        out.println("Из вершины 1 за 3 хода можно достичь: " + graph4.motionVerN(1, 3));

        out.println("\n11. СЛОЖЕНИЕ ГРАФОВ:");
        Graph graph5 = new Graph();
        graph5.add(1, 2);
        graph5.add(3, 4);

        Graph graph6 = new Graph();
        graph6.add(2, 3);
        graph6.add(4, 1);

        out.println("Граф 5:");
        out.println(graph5);
        out.println("Граф 6:");
        out.println(graph6);

        Graph sumGraph = graph5.sum(graph6);
        out.println("Результат сложения:");
        out.println(sumGraph);

        out.println("\n12. ПРОВЕРКА НА ПОЛНОТУ:");
        out.println("Граф 1 полный? " + graph1.isComplete());

        Graph completeGraph = new Graph();
        completeGraph.add(1, 2);
        completeGraph.add(1, 3);
        completeGraph.add(2, 1);
        completeGraph.add(2, 3);
        completeGraph.add(3, 1);
        completeGraph.add(3, 2);

        out.println("Полный граф (3 вершины):");
        out.println(completeGraph);
        out.println("Этот граф полный? " + completeGraph.isComplete());

        out.println("\n13. ПРОВЕРКА НА ПУСТОТУ:");
        out.println("Граф 1 пустой? " + graph1.isEmpty());

        Graph emptyGraph = new Graph();
        out.println("Новый пустой граф пустой? " + emptyGraph.isEmpty());

        out.println("\n14. ПРОВЕРКА НА НАЛИЧИЕ ТОЛЬКО ПЕТЕЛЬ:");
        out.println("Граф 1 состоит только из петель? " + graph1.isRing());

        Graph ringGraph = new Graph();
        ringGraph.add(1, 1);
        ringGraph.add(2, 2);
        ringGraph.add(3, 3);

        out.println("Граф из петель:");
        out.println(ringGraph);
        out.println("Этот граф состоит только из петель? " + ringGraph.isRing());

        out.println("\n15. ГРАФ С ОДНОЙ ВЕРШИНОЙ:");
        Graph singleVertexGraph = new Graph();
        singleVertexGraph.add(1, 1);
        out.println("Граф с одной вершиной и петлей:");
        out.println(singleVertexGraph);
        out.println("Вершины: " + singleVertexGraph.printVertexes());
        out.println("Полный? " + singleVertexGraph.isComplete());
        out.println("Состоит только из петель? " + singleVertexGraph.isRing());

        out.println("\n16. ГРАФ БЕЗ РЕБЕР:");
        Graph noEdgesGraph = new Graph();
        out.println(noEdgesGraph);
        out.println("Вершины: " + noEdgesGraph.printVertexes());
        out.println("Полный? " + noEdgesGraph.isComplete());
        out.println("Пустой? " + noEdgesGraph.isEmpty());
        out.println("Состоит только из петель? " + noEdgesGraph.isRing());
    }
}