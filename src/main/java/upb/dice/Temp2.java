package upb.dice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import upb.misc.TreeNode;

public class Temp2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static String[] getTrees(String[][] pairs) {
		List<String> resList = new ArrayList<>();
		Map<String, TreeNode<String>> nodeMap = new HashMap<>();
		for (String[] pair : pairs) {
			String hunterName = pair[0];
			String preyName = pair[1];
			TreeNode<String> hunterNode = nodeMap.get(hunterName);
			TreeNode<String> preyNode = nodeMap.get(preyName);
			if (hunterNode == null) {
				hunterNode = new TreeNode<String>(hunterName);
				nodeMap.put(hunterName, hunterNode);
			}
			if (preyNode == null) {
				preyNode = new TreeNode<String>(preyName);
				nodeMap.put(preyName, preyNode);
			}
			hunterNode.addChild(preyNode);
			preyNode.addParent(hunterNode);
		}
		int level = 0;
		// find all root nodes
		for (TreeNode<String> node : nodeMap.values()) {
			if (node.getParent() == null) {
				node.sortChildren();
				// Add Heirarchy to String
				/*while(true) {
					resList.add(getTabs(level)+node.getData());
					if()
				}*/
				break;
			}
		}
		return (String[]) resList.toArray();
	}
	
	private static String getTabs(int level) {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<level;i++) {
			sb.append("\t");
		}
		return sb.toString();
	}
	

}
