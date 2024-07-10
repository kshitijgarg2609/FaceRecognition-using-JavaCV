package com.kgprojects;

import java.util.concurrent.Semaphore;

public class TrainerLock
{
	private boolean flag = false;
	private Semaphore semaphore = new Semaphore(1, true);
	private Integer labelValue = null;
	public void setFlag(boolean flg)
	{
		try
		{
			semaphore.acquire();
			this.flag=flg;
		}
		catch(Exception e)
		{
			
		}
		semaphore.release();
	}
	public boolean getFlag()
	{
		boolean flg=false;
		try
		{
			semaphore.acquire();
			flg=flag;
		}
		catch(Exception e)
		{
			
		}
		semaphore.release();
		return flg;
	}
	public Integer getLabelValue()
	{
		return labelValue;
	}
	public void setLabelValue(Integer labelValue)
	{
		this.labelValue = labelValue;
	}
}