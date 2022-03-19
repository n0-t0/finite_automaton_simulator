import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class App {
    public static void main(String[] args) throws Exception {
        // jsonのパスを受け取り
        String path = "";
        if(args.length == 0) {
            System.out.println("引数がありません。デフォルトの./rule.jsonを読み込みますか？");
            System.out.print("y/n :");
            String ans = "";
            try(BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in){@Override public void close(){}})
            ){
                ans = br.readLine();
                System.out.println(ans);
            } catch(IOException e) {
                System.out.println("入力に失敗しました");
                e.printStackTrace();
            }
            if(ans.equals("y")) {
                path = "./rule.json";
            } else if(ans.equals("n")) {
                System.out.println("終了します");
                System.exit(1);
            } else {
                System.out.println("yかnを入力してEnterを押してください");
                System.exit(1);
            } 
        } else if(args.length == 1) {
            System.out.println(args[0]+"を読み込みます");
            path = args[0];
        } else {
            System.out.println("引数を一つ指定してください");
            System.exit(1);
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonReceiver rule = mapper.readValue(new File(path), new TypeReference<JsonReceiver>(){});


        // jsonから読んだプロパティを確認
        // state
        if(rule.getState()          == null
         ||rule.getState().size()   == 0
        ) {
            System.out.println("stateが読み込めません");
            System.exit(1);
        }
        // start_state
        if(rule.getStart_state()==null) {
            System.out.println("start_stateが読み込めません");
        } 
        // set_of_accept_states
        if(rule.getSet_of_accept_states()          == null
         ||rule.getSet_of_accept_states().size()   == 0
        ) {
            System.out.println("set_of_accept_statesが読み込めません");
            System.exit(1);
        }
        // alphabet
        if(rule.getState()          == null
         ||rule.getState().size()   == 0
        ) {
            System.out.println("alphabetが読み込めません");
            System.exit(1);
        }
        // transition_function
        // 遷移規則内に不明なアルファベット、状態が存在しない
        // 全ての遷移規則（状態, アルファベット）->状態の３つ組である
        // 遷移規則に過不足はない
        if(rule.getTransition_function()        == null
         ||rule.getTransition_function().size() == 0
        ) {
            System.out.println("getTransition_functionが読み込めません");
            System.exit(1);
        }
        if(!rule.getTransition_function().keySet().equals(rule.getState())) {
            System.out.println("getTransition_functionが正しくありません");
            System.exit(1);
        }
        // Set<String> strNextSet = new HashSet<>();
        for(Map<String, String>strArrow: rule.getTransition_function().values()) {
            if(!strArrow.keySet().equals(rule.getAlphabet())) {
                System.out.println("getTransition_functionが正しくありません");
                System.exit(1);
            }
            if(!rule.getState().containsAll(strArrow.values())) {
                System.out.println("getTransition_functionが正しくありません");
                System.exit(1);
            }
        }
        

        // 候補となる語の羅列を入力する
        System.out.print("sentence: ");
        String input = "";
        try(BufferedReader br = new BufferedReader(
            new InputStreamReader(System.in){@Override public void close(){}})
        ){
            input = br.readLine();
        } catch(IOException e) {
            System.out.println("入力に失敗しました");
            e.printStackTrace();
        }
        List<String> list = Arrays.asList(input.split(""));
        Alphabet alphabet = Alphabet.getInstance();
        List<Char> sentence = new ArrayList<>();
        for(String string: list) {
            sentence.add(alphabet.getChar(string));
        }
        // 入力されたのはアルファベットの羅列である
        for(String word: list) {
            if(!rule.getAlphabet().contains(word)) {
                System.out.println("文の候補となるのはalphabetの羅列のみです");
                System.exit(1);
            }
        }

        // インスタンスを作成
        Map<Node, Map<Char, Node>> graph = App.getGraph(rule.getTransition_function());

        // 開始状態・受理状態
        State state = State.getInstance();
        Node firstNode = state.getNode(rule.getStart_state());
        List<Node> acceptNodeList = new ArrayList<>();
        for(String acceptNode: rule.getSet_of_accept_states()) {
            acceptNodeList.add(state.getNode(acceptNode));
        }
        // 現在の状態
        Node tmpNode = firstNode;


        // 遷移のシミュレート
        Transitional func = (node, character) -> graph.get(node).get(character);

        for(Char word: sentence) {
            System.out.println("  "+tmpNode.getString());
            System.out.println(" ↓ "+word.getString());
            tmpNode = func.move(tmpNode, word);
        }

        // 結果の表示
        System.out.println("  "+tmpNode.getString());
        System.out.println(acceptNodeList.contains(tmpNode));
        
    }

    // StringのMapを展開してインスタンスを生成した後入れ直す
    public static Map<Node, Map<Char, Node>> getGraph(Map<String, Map<String, String>> strRule) {
        // 戻り値
        Map<Node, Map<Char, Node>> transRule = new HashMap<>();
        // 一時変数
        Map<String, String> strArrows = new HashMap<>();
        
        // ビルダー
        State state = State.getInstance();
        Alphabet alphabet = Alphabet.getInstance();

        // transRuleを展開
        for(String tmpNode: strRule.keySet()) {
            strArrows = strRule.get(tmpNode);
            Map<Char, Node> transArrows = new HashMap<>();

            for(String input: strArrows.keySet()) {
                String nextNode = strArrows.get(input);
                transArrows.put(alphabet.getChar(input), state.getNode(nextNode));
            }
            transRule.put(state.getNode(tmpNode), transArrows);

        }
        return transRule;
    }
}
