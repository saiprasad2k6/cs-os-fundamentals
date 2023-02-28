# Agenda
* Thread Synchronisation
* Producer-consumer problem
* Semaphores
* Concurrent data structures
    * Atomic Integer
    * Concurrent Hash Map

---


### Producer-consumer problem

The Producer-Consumer problem is a classic synchronization problem in operating systems.

The problem is defined as follows: there is a fixed-size buffer and a Producer process, and a Consumer process.

The Producer process creates an item and adds it to the shared buffer. The Consumer process takes items out of the shared buffer and “consumes” them.


Certain conditions must be met by the Producer and the Consumer processes to have consistent data synchronisation:
* The Producer process must not produce an item if the shared buffer is full.
* The Consumer process must not consume an item if the shared buffer is empty.

<img src="http://pages.cs.wisc.edu/~bart/537/lecturenotes/figures/s6-prodcons.jpg" width="200" style="margin-left:25%"/>

#### Producer
The task of the producer is to create a unit of work and add it to the store. The consumer will pick that up when it is available. The producer cannot add exceed the number of max units of the store.

```java
    public void run() {
        while (true) {
            if (store.size() < maxSizeOfStore) {
                store.add(new UnitOfWork());
            }
        }
    }
```

#### Consumer
The role of the consumer is to pick up unit of works from the queue or the store once they have been added by the producer. \
The consumer can only pick up units if there are any available.

```java
    public void run() {
        while (true) {
            if (store.size() > 0) {
                store.remove();
            }
        }
    }
```

The above code will lead to concurrency issues since multiple thread can access the store at one time.
What happens if there is only one unit present, but two consumers try to acquire it at the same time.
Since the size of the store will be 1, both of them will be allowed to execute, but one will error out.

---
## Solution to Synchronisation

Below  Conditions to follow while threads enter the critical section
* `Mutual Exclusion` - Only one thread to enter the Critical Section at one time. This will prevent the race condition.
* `Progress` - Overall System should keep working and making progress
* `Bounded Waiting` - No thread should have to wait infinitely. Bound on the aiting time for any thread to enter the Critical Section.
* `No Busy Waiting` - No thread should not have to continuously check if they can enter the critical section or not, ideal solution needs to have a notification.

### Base Solution - Mutex

In order to solver the concurrency issue, we can use mutexes or locks.
In Java this can be achieved by simply wrapping the critical section in a synchronised block.


```java
    public void run() {
        while (true) {
            synchronized (store) {
                if (store.size() < maxSizeOfStore) {
                    store.add(new Shirt());
                }
            }
        }
    }
```

Now only one thread will be able to access the store at one time. This will solve the concurrency issue, but our execution does not happen in parallel now. Due to the mutex, only one thread can access the store at a time.

### Parallel solution - Semaphore

A semaphore is a variable or abstract data type used to control access to a common resource by multiple threads and avoid critical section problems in a concurrent system such as a multitasking operating system. The main attribute of a semaphore is how many thread does it control. If a semaphore handles just one thread, it is effectively similar to a mutex.

To solve our producer and consumer problem, we can use two semaphores:
1. For Producer - This semaphore will control the maximum number of producers and is initialised with the max stor size.
2. For Consumer - This semaphore controls the maximum number of consumers. This starts with 0 active threads.

```java
Semaphore forProducer = new Semaphore(maxSize);
Semaphore forConsumer = new Semaphore(0);
```

The ideal situation for us is that:
* There are parallel threads that are able to produce until all the store is filled.
* There are parallel threads that are able to consume until all the store is empty.

Thus, to achieve this we use each producer thread to signal to the consumer that it has added a new unit. Similarly, once the consumer has reduced the unit of works, it signals it to the producer to start making more.

```java

// Producer

while (true) {
    forProducer.acquire();
    store.add(new UnitOfWork());
    forConsumer.release();
}
```

```java

// Consumer

while (true) {
    forConsumer.acquire();
    store.add(new UnitOfWork());
    forProducer.release();
}
```

## Concurrent Data structures

A concurrent data structure is a particular way of storing and organizing data for access by multiple computing threads (or processes) on a computer. A shared mutable state very easily leads to problems when concurrency is involved. If access to shared mutable objects is not managed properly, applications can quickly become prone to some hard-to-detect concurrency errors.

Some common concurrent data structures:
1. [Atomic Integer](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/atomic/AtomicInteger.html#:~:text=An%20AtomicInteger%20is%20used%20in,deal%20with%20numerically%2Dbased%20classes) 
    > The AtomicInteger class protects an underlying int value by providing methods that perform atomic operations on the value
2. [Concurrent hash maps](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentHashMap.html)

## Deadlocks

A deadlock in OS is a situation in which more than one process is blocked because it is holding a resource and also requires some resource that is acquired by some other process.

### Conditions for a deadlock
* `Mutual exclusion` - The resource is held by only one process at a time and cannot be acquired by another process.
* `Hold and wait` - A process is **holding** a resource and **waiting** for another resource to be released by another a process.
* `No preemption` - The resource can only be released once the execution of the process is complete.
* `Circular wait` - A set of processes are waiting for each other circularly. Process `P1` is waiting for process `P2` and process `P2` is waiting for process `P1`.

<img src="https://scaler.com/topics/images/deadlock-in-os-image1.webp" width="300" style="margin-left:25%"/>


Process P1 and P2 are in a deadlock because:
* Resources are non-shareable. (Mutual exclusion)
* Process 1 holds "Resource 1" and is waiting for "Resource 2" to be released by process 2. (Hold and wait)
* None of the processes can be preempted. (No preemption)
* "Resource 1" and needs "Resource 2" from Process 2 while Process 2 holds "Resource 2" and requires "Resource 1" from Process 1. (Circular wait)

### Tackling deadlocks

There are three ways to tackle deadlocks:
* Prevention - Implementing a mechanism to prevent the deadlock.
* Avoidance - Avoiding deadlocks by not allocating resources when deadlocks are possible.
* Detecting and recovering - Detecting deadlocks and recovering from them.
* Ignorance - Ignore deadlocks as they do not happen frequently.

#### Prevention and avoidance

Deadlock prevention means to block at least one of the four conditions required for deadlock to occur. If we are able to block any one of them then deadlock can be prevented. Spooling and non-blocking synchronization algorithms are used to prevent the above conditions. In deadlock prevention all the requests are granted in a finite amount of time.

In Deadlock avoidance we have to anticipate deadlock before it really occurs and ensure that the system does not go in unsafe state.It is possible to avoid deadlock if resources are allocated carefully. For deadlock avoidance we use Banker’s and Safety algorithm for resource allocation purpose. In deadlock avoidance the maximum number of resources of each type that will be needed are stated at the beginning of the process.

#### Detecting and recovering from deadlocks

We let the system fall into a deadlock and if it happens, we detect it using a detection algorithm and try to recover.

Some ways of recovery are as follows:

* Aborting all the deadlocked processes.
* Abort one process at a time until the system recovers from the deadlock.
* Resource Preemption: Resources are taken one by one from a process and assigned to higher priority processes until the deadlock is resolved.

#### Ignorance

The system assumes that deadlock never occurs. Since the problem of deadlock situation is not frequent, some systems simply ignore it. Operating systems such as UNIX and Windows follow this approach. However, if a deadlock occurs we can reboot our system and the deadlock is resolved automatically.

#### Tackling deadlocks at an application level
* Set timeouts for all the processes. If a process does not respond within the timeout period, it is killed.
* Implementing with caution: Use interfaces that handle or provide callbacks if locks are held by other processes.
* Add timeout to locks: If a process requests a lock, and it is held by another process, it will wait for the lock to be released until the timeout expires.
