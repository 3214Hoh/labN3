import java.io.PrintStream;
import java.util.*;

class Graph {
    public static Scanner in = new Scanner(System.in);
    public static PrintStream out = System.out;

    private String[] lst;
    private int size;

    public Graph() {
        this.lst = new String[1000000];
        this.size = 0;
    }

    //Вывод всех ребер графа
    @Override
    public String toString() {
        if(size == 0) {
            return "Граф не содержит рёбер.";
        }
        String out1 = "";
        for(int i = 0; i < size; i++) {
            out1 += lst[i] + "\n";
        }
        return out1;
    }

    //Добавление ребер
    public void add(int start, int end) {
        String edge = start + " -> " + end;
        // Проверка на дубликаты
        for(int i = 0; i < size; i++) {
            if(lst[i].equals(edge)) {
                return;
            }
        }
        //Добавление
        lst[size] = edge;
        size++;
    }

    //Вывод вершин по убыванию номера
    public String printVertexes() {
        Set<Integer> vertexes = new HashSet<>();
        //Извлечение вершин из строковой записи
        for(int i = 0; i < size; i++) {
            String[] parts = lst[i].split(" -> ");
            vertexes.add(Integer.parseInt(parts[0]));
            vertexes.add(Integer.parseInt(parts[1]));
        }
        //Сортировка
        Integer[] result = vertexes.toArray(new Integer[0]);
        Arrays.sort(result, Collections.reverseOrder());
        //Вывод
        String out1 = "";
        for(int i : result) {
            out1 += i + " ";
        }
        return out1;
    }

    //Вершины с входящими рёбрами больше заданного числа
    public String VertexesMoreN(int n) {
        if(size == 0) return "Нет таких вершин";
        //Создаем Map для подсчета входящих ребер
        Map<Integer, Integer> incomingCount = new HashMap<>();

        for(int i = 0; i < size; i++) {
            String[] parts = lst[i].split(" -> ");
            int to = Integer.parseInt(parts[1]);
            incomingCount.put(to, incomingCount.getOrDefault(to, 0) + 1);
        }
        //Заполняем массив вершинами с количеством входящих ребер > n
        Integer[] tempResult = new Integer[incomingCount.size()];
        int count = 0;
        for(Map.Entry<Integer, Integer> entry : incomingCount.entrySet()) {
            if(entry.getValue() > n) {
                tempResult[count++] = entry.getKey();
            }
        }
        if(count == 0) return "Нет таких вершин";
        //Создаем массив нужного размера
        Integer[] result = new Integer[count];
        for(int i = 0; i < count; i++) {
            result[i] = tempResult[i];
        }
        Arrays.sort(result);
        //Вывод
        String out1 = "";
        for(int i = 0; i < count; i++) {
            out1 += result[i] + " ";
        }
        return out1;
    }

    //Удаление ребра
    public boolean deleteReb(int start, int end) {
        String edge = start + " -> " + end;
        for(int i = 0; i < size; i++) {
            if(lst[i].equals(edge)) {
                //Сдвиг элементов
                for(int j = i; j < size - 1; j++) lst[j] = lst[j + 1];
                size--;
                return true;
            }
        }
        return false;
    }

    //Удаление вершины
    public boolean deleteVer(int id) {
        boolean fg = false;
        int i = 0;
        while(i < size) {
            String[] parts = lst[i].split(" -> ");
            int from = Integer.parseInt(parts[0]);
            int to = Integer.parseInt(parts[1]);
            if(from == id || to == id) {
                //Удаление элемента
                for(int j = i; j < size - 1; j++) {
                    lst[j] = lst[j + 1];
                }
                size--;
                fg = true;
            }
            else i++;
        }
        return fg;
    }

    //Удаление вершин с наибольшим количеством входящих рёбер
    public boolean deleteMinVer() {
        if(size == 0) return false;
        //Подсчет входящих ребер
        Map<Integer, Integer> lst_vr = new HashMap<>();
        for(int i = 0; i < size; i++) {
            String[] parts = lst[i].split(" -> ");
            int to = Integer.parseInt(parts[1]);
            lst_vr.put(to, lst_vr.getOrDefault(to, 0) + 1);
        }
        if(lst_vr.isEmpty()) return false;
        //Находим вершину с максимальным количеством входящих ребер
        int max_id = -1;
        int max_cnt = -1;
        for(Map.Entry<Integer, Integer> entry : lst_vr.entrySet()) {
            if(entry.getValue() > max_cnt) {
                max_cnt = entry.getValue();
                max_id = entry.getKey();
            }
        }
        return deleteVer(max_id);
    }

    //Поиск компонент связности
    public Set<Set<Integer>> searchComp() {
        //Создаем список смежности (неориентированный граф)
        Map<Integer, Set<Integer>> groups = new HashMap<>();

        for(int i = 0; i < size; i++) {
            String[] parts = lst[i].split(" -> ");
            int a = Integer.parseInt(parts[0]);
            int b = Integer.parseInt(parts[1]);
            groups.computeIfAbsent(a, k -> new HashSet<>()).add(b);
            groups.computeIfAbsent(b, k -> new HashSet<>()).add(a);
        }

        Set<Integer> visited = new HashSet<>();
        Set<Set<Integer>> result = new HashSet<>();

        for(Integer i : groups.keySet()) {
            if(!visited.contains(i)) {
                Set<Integer> component = new HashSet<>();
                dfs(i, groups, visited, component);
                result.add(component);
            }
        }
        return result;
    }

    //Для группировки вершин
    private void dfs(int vertex, Map<Integer, Set<Integer>> groups,
                     Set<Integer> visited, Set<Integer> component) {
        //component - группа обосбленных вершин
        visited.add(vertex);
        component.add(vertex);

        for(int neighbor : groups.getOrDefault(vertex, new HashSet<>())) {
            if(!visited.contains(neighbor)) {
                dfs(neighbor, groups, visited, component);
            }
        }
    }

    //Вершины, достижимые за 2 хода
    public Set<Integer> motionVer2(int startId) {
        return motionVerN(startId, 2);
    }

    //Вершины, достижимые за заданное количество ходов
    public Set<Integer> motionVerN(int start, int n) {
        if(n <= 0) return new HashSet<>();

        // Создаем список смежности
        Map<Integer, Set<Integer>> lst_vr = new HashMap<>();
        for(int i = 0; i < size; i++) {
            String[] parts = lst[i].split(" -> ");
            int a = Integer.parseInt(parts[0]);
            int b = Integer.parseInt(parts[1]);
            lst_vr.computeIfAbsent(a, k -> new HashSet<>()).add(b);
        }

        Set<Integer> current = new HashSet<>();
        current.add(start);
        Set<Integer> result = new HashSet<>();

        for(int i = 1; i <= n; i++) {
            Set<Integer> next = new HashSet<>();
            for(int j : current) {
                Set<Integer> neighbors = lst_vr.getOrDefault(j, new HashSet<>());
                next.addAll(neighbors);
            }
            result.addAll(next);
            current = next;
            if(current.isEmpty()) break;
        }
        result.remove(start);
        return result;
    }

    //Сложение двух графов
    public Graph sum(Graph other) {
        Graph result = new Graph();
        //Копируем все ребра из текущего графа
        for(int i = 0; i < this.size; i++) {
            String[] parts = this.lst[i].split(" -> ");
            result.add(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }
        //Добавляем ребра из второго графа
        if(other != null) {
            for(int i = 0; i < other.size; i++) {
                String[] parts = other.lst[i].split(" -> ");
                result.add(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            }
        }
        return result;
    }

    //Проверка на полноту
    public boolean isComplete() {
        // Получаем все вершины графа
        Set<Integer> lst_vr = new HashSet<>();
        for(int i = 0; i < size; i++) {
            String[] parts = lst[i].split(" -> ");
            lst_vr.add(Integer.parseInt(parts[0]));
            lst_vr.add(Integer.parseInt(parts[1]));
        }
        //Если вершин меньше 2, граф полный
        if(lst_vr.size() < 2) return true;
        //Преобразуем Set в массив
        int vertex_cnt = lst_vr.size();
        int[] lst_vertices = new int[vertex_cnt];
        int index = 0;

        for(Integer vertex : lst_vr) lst_vertices[index++] = vertex;
        // Проверяем наличие всех возможных ребер
        for(int i = 0; i < vertex_cnt; i++) {
            for(int j = 0; j < vertex_cnt; j++) {
                if(i != j) {
                    String edge = lst_vertices[i] + " -> " + lst_vertices[j];
                    boolean found = false;

                    //Поиск ребра
                    for(int k = 0; k < size; k++) {
                        if(lst[k].equals(edge)) {
                            found = true;
                            break;
                        }
                    }
                    if(!found) return false;
                }
            }
        }
        return true;
    }

    //Проверка на пустоту графа
    public boolean isEmpty() {
        return size == 0;
    }

    //Проверка на наличие вершин, соединённых только с собой
    public boolean isRing() {
        if(size == 0) return false;
        for(int i = 0; i < size; i++) {
            String[] parts = lst[i].split(" -> ");
            int x1 = Integer.parseInt(parts[0]);
            int x2 = Integer.parseInt(parts[1]);
            if(x1 != x2) return false;
        }
        return true;
    }
}


