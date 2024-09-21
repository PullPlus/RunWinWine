package models;

import java.nio.file.Path;

public class Shortcut {
	
	private String[] _envVarsArray;
	private Path _execPath;
	private String[] _args;
	private String _name;
	private String _description;

    public String getDescription() {
        return _description;
    }

    public Path getExecPath() {
        return _execPath;
    }

    public String getName() {
        return _name;
    }
    
    public String[] getArgs() {
		return _args;
	}
    
    public String[] getEnvVarsArray() {
		return _envVarsArray;
	}
    
    public Shortcut(String[] envVarsArray, Path execPath, String[] args, String name, String description) {
    	_envVarsArray = envVarsArray;
        _description = description;
        _execPath = execPath;
        _name = name;
        _args = args;
    }
}
