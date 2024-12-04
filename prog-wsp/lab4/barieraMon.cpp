#include <pthread.h>
#include <iostream>

class Barrier {
private:
    int threshold;
    int count;
    pthread_mutex_t mutex;
    pthread_cond_t condition;

public:
    Barrier(int n) : threshold(n), count(0) {
        pthread_mutex_init(&mutex, nullptr);
        pthread_cond_init(&condition, nullptr);
    }

    ~Barrier() {
        pthread_mutex_destroy(&mutex);
        pthread_cond_destroy(&condition);
    }

    void wait() {
        pthread_mutex_lock(&mutex);
        count++;
        if (count == threshold) {
            pthread_cond_broadcast(&condition);
            count = 0; // Reset for reuse
        } else {
            pthread_cond_wait(&condition, &mutex);
        }
        pthread_mutex_unlock(&mutex);
    }
};
