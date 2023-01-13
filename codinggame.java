import java.util.Scanner;

class Player {                                             /*введем некоторые заданные в задачи условия */
	private static  int MAX_VERTICAL_SPEED = 40;
	private static  int MAX_HORIZONTAL_SPEED = 20;
	private static double g = 3.711;
	private static int mindistancefromflat = 50;
	private static int minnumber = 10; 
    
	public static void main(String args[]) {
    	
        Scanner in = new Scanner(System.in);            /* строим surface и узнаем нашу площадку приземления */
        int surfaceN = in.nextInt();                     
        
        int flatGroundLeftX = 0;
        int flatGroundRightX = 0;
        int flatGroundY = 0;
        
        int previousPointX = 0;
        int previousPointY = 0;
        for (int i = 0; i < surfaceN; i++) {
            int landX = in.nextInt(); 
            int landY = in.nextInt();
            if (previousPointY == landY) {
            	flatGroundLeftX = previousPointX;
            	flatGroundRightX = landX;
            	flatGroundY = landY;
            } else {
            	previousPointX = landX;
            	previousPointY = landY;
            }
        }
        
        // game loop
        while (true) {
            int X = in.nextInt();
            int Y = in.nextInt();
            int HS = in.nextInt(); // the horizontal speed (in m/s), can be negative.
            int VS = in.nextInt(); // the vertical speed (in m/s), can be negative.
            int F = in.nextInt(); // the quantity of remaining fuel in liters.
            int R = in.nextInt(); // the rotation angle in degrees (-90 to 90).
            int P = in.nextInt(); // the thrust power (0 to 4).

/*  при каких условиях наш лаундер над платформой */

            if (isMarsLanderFlyingOverFlatGround(X, flatGroundLeftX, flatGroundRightX)) { 
            	
            	if (isMarsLanderAboutToLand(Y, flatGroundY)) {
            		R = 0;
            		P = 3;
            	} else if (areMarsLanderSpeedLimitsSatisfied(HS, VS)) {
            		R = 0;
            		P = 2;
            	} else {
            		R = calculateRotationToSlowDownMarsLander(HS, VS);
            		P = 4;
            	}

            } else {

/* в каких случаях наш лаундер летит не туда  */

            	if (isMarsLanderFlyingInTheWrongDirection(X, HS, flatGroundLeftX, flatGroundRightX) || isMarsLanderFlyingTooFastTowardsFlatGround(HS)) {
            		R = calculateRotationToSlowDownMarsLander(HS, VS);
            		P = 4;
            	} else if (isMarsLanderFlyingTooSlowTowardsFlatGround(HS)) {
            		R = calculateRotationToSpeedUpMarsLanderTowardsFlatGround(X, flatGroundLeftX, flatGroundRightX);
            		P = 4;
            	} else {
            		R = 0;
            		P = calculateThrustPowerToFlyTowardsFlatGround(VS);
            	}
            	
            }
            
            System.out.println(R + " " + P);
        }
        
    }

	public static boolean isMarsLanderFlyingOverFlatGround(int marsLanderX, int flatGroundLeftX, int flatGroundRightX) {
		return marsLanderX >= flatGroundLeftX && marsLanderX <= flatGroundRightX;
	}
	
	public static boolean isMarsLanderAboutToLand(int marsLanderY, int flatGroundY) {
		return marsLanderY < flatGroundY + mindistancefromflat;
	}
	
	public static boolean areMarsLanderSpeedLimitsSatisfied(int marsLanderHorizontalSpeed, int marsLanderVerticalSpeed) {
		return Math.abs(marsLanderHorizontalSpeed) <= (MAX_HORIZONTAL_SPEED - minnumber) && Math.abs(marsLanderVerticalSpeed) <= (MAX_VERTICAL_SPEED - minnumber);
	}
/* найдем угол вращения лаундера для снижения скорости  */
	public static int calculateRotationToSlowDownMarsLander(int horizontalSpeed, int verticalSpeed) {
		double speed = Math.sqrt(Math.pow(horizontalSpeed, 2) + Math.pow(verticalSpeed, 2));
		double rotationAsRadians = Math.asin((double) horizontalSpeed / speed);
		return (int) Math.toDegrees(rotationAsRadians);
	}
	
	public static boolean isMarsLanderFlyingInTheWrongDirection(int marsLanderX, int marsLanderHorizontalSpeed, int flatGroundLeftX, int flatGroundRightX) {
		
		if (marsLanderX < flatGroundLeftX && marsLanderHorizontalSpeed < 0) {
			return true;
		}
		
		if (marsLanderX > flatGroundRightX && marsLanderHorizontalSpeed > 0) {
			return true;
		}
		
		return false;
	}
	
/*нужно чтобы скорость вертикал и горизонт соотв условию задачи */
	public static boolean isMarsLanderFlyingTooFastTowardsFlatGround(int marsLanderHorizontalSpeed) {
		return Math.abs(marsLanderHorizontalSpeed) > (MAX_HORIZONTAL_SPEED * 4);
	}
	
	public static boolean isMarsLanderFlyingTooSlowTowardsFlatGround(int marsLanderHorizontalSpeed) {
		return Math.abs(marsLanderHorizontalSpeed) < (MAX_HORIZONTAL_SPEED * 2);
	}

/* формула угла была взята из форума ) */
	public static int calculateRotationToSpeedUpMarsLanderTowardsFlatGround(int marsLanderX, int flatGroundLeftX, int flatGroundRightX) {
		
		if (marsLanderX < flatGroundLeftX) {
			return - (int) Math.toDegrees(Math.acos(g / 4.0));  
		}
		
		if (marsLanderX > flatGroundRightX) {
			return + (int) Math.toDegrees(Math.acos(g / 4.0)); 
		}
		
		return 0;
	}
	
	public static int calculateThrustPowerToFlyTowardsFlatGround(int marsLanderVerticalSpeed) {
		return (marsLanderVerticalSpeed >= 0) ? 3 : 4;
	}
	
}