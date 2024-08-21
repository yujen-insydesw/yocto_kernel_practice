#include <stdio.h>

void swap(int *, int*);

int main(void)
{
    int a = 10;
    int b = 20;
    printf("The old values: a = %d, b = %d.\n", a, b);
    swap(&a, &b);
    printf("The new values: a = %d, b = %d.\n", a, b);
    return 0;
}

void swap(int *p1, int *p2)
{
    p1 = p1 ^ p2;
    p2 = p1 ^ p2;
    p1 = p1 ^ p2;
}