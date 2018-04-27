package com.gautsch.myapplication;


public class ScpConnection {
    private ScpConnection instance;

    private ScpConnection() {}

    private ScpConnection getInstance()
    {
      if (instance == null)
      {
          instance = new ScpConnection();
      }

      return instance;
    }


}
