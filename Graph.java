import java.io.PrintStream;
import java.util.*;

class Graph {
    public static Scanner in = new Scanner(System.in);
    public static PrintStream out = System.out;

    private List<String> lst;

    public Graph(List<String> edges) {
        this.lst = new ArrayList<>();
    }

    //Вывод всех ребер графа
    @Override
    public String toString() {
        if(lst.isEmpty()) {
            return "Граф не содержит рёбер.";
        }
        String out1 = "";
        for(String edge : lst) {
            out1 += edge + "\n";
        }
        return out1;
    }

    //Добавление ребер
    public void add(int start, int end) {
        String edge = start + " -> " + end;
        if(!lst.contains(edge)) {
            lst.add(edge);
        }
    }

    //Вывод вершин по убыванию номера
    public String printVertexes() {
        Set<Integer> vertexes = new HashSet<>();
        //Извлечение вершин из строковой записи
        for(String i : lst) {
            String[] parts = i.split(" -> ");
            vertexes.add(Integer.parseInt(parts[0]));
            vertexes.add(Integer.parseInt(parts[1]));
        }
        //Сортировка
        List<Integer> result = new ArrayList<>(vertexes);
        result.sort(Collections.reverseOrder());
        //Вывод
        String out1 = "";
        for(int i : result) {
            out1 += i + " ";
        }
        return out1;
    }

    //Вершины с входящими рёбрами больше заданного числа
    public String VertexesMoreN(int n) {
        //Массив всех вершин, с повторами
        int[] lst_ver = new int[lst.size()];
        int cnt = 0;
        for(String i : lst) {
            String[] parts = i.split(" -> ");
            lst_ver[cnt] = Integer.parseInt(parts[1]);
            cnt++;
        }
        Arrays.sort(lst_ver);
        int id = lst_ver[0];
        cnt = 1;
        String out1 = "";
        boolean fg = true;
        //Заполнение строки с вершинами, которые больше n
        for(int i = 1; i < lst_ver.length; i++) {
            if(lst_ver[i] == id) cnt++;
            else {
                if(cnt > n) {
                    out1 += id + " ";
                    fg = false;
                }
                id = lst_ver[i];
                cnt = 1;
            }
        }
        if(fg) return "Нет таких вершин";
        return out1;
    }

    //Удаление ребра
    public boolean deleteReb(int start, int end) {
        String edge = start + " -> " + end;
        boolean fg = lst.remove(edge);
        return fg;
    }

    //Удаление вершины
    public boolean deleteVer(int id) {
        List<String> copy_lst = new ArrayList<>();
        boolean fg = false;
        //Добавляем в список пары, которые надо удалить
        for(String edge : lst) {
            String[] parts = edge.split(" -> ");
            int from = Integer.parseInt(parts[0]);
            int to = Integer.parseInt(parts[1]);

            if(from == id || to == id) {
                copy_lst.add(edge);
                fg = true;
            }
        }
        // Удаляем найденные рёбра
        lst.removeAll(copy_lst);
        return fg;
    }

    //Удаление вершин с наименьшим количеством входящих рёбер (без учёта петель)
    public boolean deleteMinVer() {
        if(lst.isEmpty()) return false;
        int[] lst_ver = new int[lst.size()];
        int cnt = 0;
        for(String i : lst) {
            String[] parts = i.split(" -> ");
            lst_ver[cnt] = Integer.parseInt(parts[1]);
            cnt++;
        }
        Arrays.sort(lst_ver);
        int[] lst_vr = new int[cnt];
        int[] lst_vr2 = new int[cnt];
        int id = lst_ver[0];
        cnt = 1;
        //Заполнение массива всех вершин и количества в них вхождений
        for(int i = 1; i < lst_ver.length; i++) {
            if(lst_ver[i] == id) cnt++;
            else {
                lst_vr[i-1] = cnt;
                lst_vr2[i-1] = id;
                id = lst_ver[i];
                cnt = 1;
            }
        }
        //нахождение индекса вершины с большим вхождением
        int max_id = -1;
        int max_count = -1;
        for(int i = 0; i < lst_vr.length; i++) {
            if(lst_vr[i] > max_count) {
                max_id = i;
                max_count = lst_vr[i];
            }
        }
        deleteVer(lst_vr2[max_id]);
        return true;
    }

    //Подсчёт количества компонент связности
    public int countComp() {
        if(lst.isEmpty()) return 0;
        return searchComp().size();
    }

    //Поиск компонент связности
    public Set<Set<Integer>> searchComp() {
        Map<Integer, Set<Integer>> groups = new HashMap<>();

        for(String i : lst) {
            String[] parts = i.split(" -> ");
            int a = Integer.parseInt(parts[0]);
            int b = Integer.parseInt(parts[1]);
            //Объединяем группы вершин
            Set<Integer> group = new HashSet<>();
            group.add(a);
            group.add(b);
            groups.merge(a, group, (old, n) -> {old.addAll(n); return old;});
            groups.merge(b, groups.get(a), (old, n) -> {old.addAll(n); return old;});
        }
        //Собираем уникальные группы
        Set<Set<Integer>> result = new HashSet<>(groups.values());
        return result;
    }

    //Вершины, достижимые за 2 хода
    public Set<Integer> motionVer2(int startId) {
        Set<Integer> reachable = new HashSet<>();
        //прямые соседи
        Set<Integer> firstStep = new HashSet<>();
        for(String edge : lst) {
            String[] parts = edge.split(" -> ");
            int x1 = Integer.parseInt(parts[0]);
            int x2 = Integer.parseInt(parts[1]);
            if(x1 == startId) {
                firstStep.add(x2);
            }
        }

        //соседи соседей
        for(int i : firstStep) {
            for(String edge : lst) {
                String[] parts = edge.split(" -> ");
                int from = Integer.parseInt(parts[0]);
                int to = Integer.parseInt(parts[1]);

                if(from == i) {
                    reachable.add(to);
                }
            }
        }

        // Добавляем вершины первого шага (достижимые за 1 ход)
        reachable.addAll(firstStep);

        // Убираем стартовую вершину из результата
        reachable.remove(startId);

        return reachable;
    }

    //Вершины, достижимые за заданное количество ходов
    public Set<Integer> motionVerN(int start, int n) {
        // Если количество шагов неположительное, возвращаем пустое множество
        if(n <= 0) {
            return new HashSet<>();
        }

        // Создаем список смежности (ориентированный)
        Map<Integer, Set<Integer>> lst_vr = new HashMap<>();
        for(String i : lst) {
            String[] parts = i.split(" -> ");
            int x1 = Integer.parseInt(parts[0]);
            int x2 = Integer.parseInt(parts[1]);
            lst_vr.computeIfAbsent(x1, k -> new HashSet<>()).add(x2);
        }

        //Множество для хранения результата
        Set<Integer> result = new HashSet<>();

        recursionSossed(start, n, lst_vr, result);
        // Убираем стартовую вершину из результата
        result.remove(start);

        return result;
    }

    //рекурсия для поиска соседей
    private void recursionSossed(int start, int step,
                                 Map<Integer, Set<Integer>> edge,
                                 Set<Integer> result) {
        if(step == 0) {
            return;
        }
        //Соседи текущей вершины, если их нет, то пустое множество
        Set<Integer> ver = edge.getOrDefault(start, new HashSet<>());
        //Добавление всех соседей в результат
        for(int i : ver) {
            result.add(i);
            recursionSossed(i, step - 1, edge, result);
        }
    }

    //Сложение двух графов
    public Graph sum(Graph other) {
        //Создает новый граф для результата
        Graph result = new Graph(new ArrayList<>());
        //Копирует все ребра из текущего графа
        for(String edge : this.lst) {
            result.lst.add(edge);
        }
        //Добавляем ребра из второго графа, исключая дубликаты
        if(other != null) {
            for(String i : other.lst) {
                if(!result.lst.contains(i)) {
                    result.lst.add(i);
                }
            }
        }
        return result;
    }

    //Проверка на полноту графа
    public boolean isComplete() {
        // Получаем все вершины графа
        Set<Integer> lst_vr = new HashSet<>();
        for(String i : lst) {
            String[] parts = i.split(" -> ");
            lst_vr.add(Integer.parseInt(parts[0]));
            lst_vr.add(Integer.parseInt(parts[1]));
        }

        // Если вершин меньше 2, граф полный (нет пар для проверки)
        if(lst_vr.size() < 2) {
            return true;
        }

        // Проверяем наличие всех возможных ребер между различными вершинами
        List<Integer> lst_vr_copy = new ArrayList<>(lst_vr);
        for(int i = 0; i < lst_vr_copy.size(); i++) {
            for(int j = 0; j < lst_vr_copy.size(); j++) {
                if(i != j) { // Исключаем петли
                    int from = lst_vr_copy.get(i);
                    int to = lst_vr_copy.get(j);
                    String x = from + " -> " + to;

                    //Если отсутствует ребро между различными вершинами, граф не полный
                    if(!lst.contains(x)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    //Проверка на пустоту графа
    public boolean isEmpty() {
        return lst.isEmpty();
    }

    //Проверка на наличие вершин, соединённых только с собой
    public boolean isRing() {
        if(lst.isEmpty()) {
            return false;
        }
        for(String i : lst) {
            String[] parts = i.split(" -> ");
            int x1 = Integer.parseInt(parts[0]);
            int x2 = Integer.parseInt(parts[1]);
            if(x1 != x2) return false;
        }
        return true;
    }
}
