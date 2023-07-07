public class Ride {
    // Abstraction (User Interface)
    interface RideBooking {
        void bookRide();
        void cancelRide();
        void getDriverInfo();
    }
    // Implementor (Ride Service)
    interface RideService {
        void requestRide();
        void cancelRide();
        void getDriverDetails();
    }
    // Refined Abstraction (User Interface Implementation)
    class UserApp implements RideBooking {
        private RideService rideService;

        public UserApp(RideService rideService) {
            this.rideService = rideService;
        }

        @Override
        public void bookRide() {
            rideService.requestRide();
        }

        @Override
        public void cancelRide() {
            rideService.cancelRide();
        }

        @Override
        public void getDriverInfo() {
            rideService.getDriverDetails();
        }
    }
    // Concrete Implementor (Grab Car Service)
    class GrabCarService implements RideService {
        @Override
        public void requestRide() {
            System.out.println("GrabCar: Requesting a ride...");
            // Gửi yêu cầu đặt xe GrabCar
        }

        @Override
        public void cancelRide() {
            System.out.println("GrabCar: Cancelling the ride...");
            // Hủy yêu cầu đặt xe GrabCar
        }

        @Override
        public void getDriverDetails() {
            System.out.println("GrabCar: Getting driver details...");
            // Lấy thông tin về tài xế của GrabCar
        }
    }
    public class Main {
        public static void main(String[] args) {
            RideService grabCarService = new GrabCarService();
            RideBooking userApp = new UserApp(grabCarService);

            userApp.bookRide();
            userApp.getDriverInfo();
            userApp.cancelRide();
        }
    }

}
