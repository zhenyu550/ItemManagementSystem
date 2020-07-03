package other;

public class CheckString {
	public boolean isNullOrWhiteSpace(String input)
	{
	    return input == null || isWhitespace(input) || input.length() == 0;
	}
	
	public boolean isNullOrEmpty(String input)
	{
	    return input == null || input.length() == 0;
	}
	
	private boolean isWhitespace(String s) {
	    int length = s.length();
	    if (length > 0) {
	        for (int i = 0; i < length; i++) {
	            if (!Character.isWhitespace(s.charAt(i))) {
	                return false;
	            }
	        }
	        return true;
	    }
	    return false;
	}
	
	public boolean isDigit(String input)
	{
		return input.matches("[0-9]+");
	}
	
	public boolean isValidIc(String input)
	{
		if(isDigit(input))
		{
			if(input.length() <= 12)
				return true;
		}
		return false;
	}
	
	public boolean isValidContactNo(String input)
	{
		if(isDigit(input))
		{
			if(input.length() <= 20)
				return true;
		}
		return false;
	}
	
	public boolean isOverLimit(String input, int limit)
	{
		if(input.length() > limit)
			return true;
		else
			return false;
	}

}
