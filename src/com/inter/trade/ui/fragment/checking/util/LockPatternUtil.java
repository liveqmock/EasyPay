package com.inter.trade.ui.fragment.checking.util;

import java.util.List;

import com.inter.trade.ui.fragment.checking.LockPatternView.Cell;

/**
 * 手势密码帮助类
 * @author zhichao.huang
 *
 */
public class LockPatternUtil {
	
	/**
	 * 手势密码转为对应的String
	 * 把手势密码结果转为一串数字，对应012345678
	 * @return
	 */
	public static String lockPatternToString(List<Cell> cells) {
		
		String lockstr = "";
		for(Cell cell : cells) {
			
			if(cell.getRow() == 0 && cell.getColumn() == 0) {
				lockstr += 0;
			}
			if(cell.getRow() == 0 && cell.getColumn() == 1) {
				lockstr += 1;
			}
			if(cell.getRow() == 0 && cell.getColumn() == 2) {
				lockstr += 2;
			}
			if(cell.getRow() == 1 && cell.getColumn() == 0) {
				lockstr += 3;
			}
			if(cell.getRow() == 1 && cell.getColumn() == 1) {
				lockstr += 4;
			}
			if(cell.getRow() == 1 && cell.getColumn() == 2) {
				lockstr += 5;
			}
			if(cell.getRow() == 2 && cell.getColumn() == 0) {
				lockstr += 6;
			}
			if(cell.getRow() == 2 && cell.getColumn() == 1) {
				lockstr += 7;
			}
			if(cell.getRow() == 2 && cell.getColumn() == 2) {
				lockstr += 8;
			}
			
		}
		
		return lockstr;
		
	}

}
