# 동시성 제어 방식에 대한 분석 및 보고서

여러 사용자가 동일한 정보에 접근하려고 할 때 발생할 수 있는 문제를 해결하기 위해 동시성 제어가 필요하다. Java에서 제공하는 두 가지 동시성 제어 기법인 synchronized와 ReentrantLock을 활용하여 이 문제를 해결할 수 있다.

# synchronized
synchronized 키워드는 Java에서 기본적인 동시성 제어 방법으로, 한 번에 하나의 스레드만 특정 코드 블록이나 메서드에 접근할 수 있도록 제한합니다. 이 방식은 데이터 무결성을 보장하지만, 모든 사용자에게 동시에 적용되므로 한 명의 사용자만 정보에 접근할 수 있는 상황이 발생할 수 있습니다. 이로 인해 성능 저하와 병목 현상이 발생할 수 있으며, 이때 성능 저하가 발생할 수 있습니다.

# ReentrantLock
synchronized의 성능 저하를 해결하기 위한 방안으로 채택된 방안인 ReentrantLock은 Java의 java.util.concurrent.locks 패키지에 포함된 클래스으로, 보다 유연하고 강력한 동시성 제어 기능을 제공합니다. 각 사용자가 자신만의 잠금을 가지므로 여러 사용자가 동시에 서로 다른 정보에 접근할 수 있습니다.