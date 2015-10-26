/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lcplos.dataStructures;

/**
 *
 * @author elias
 */
public abstract class Minheap {

    private int[] heap;
    private int heapsize;

    private int[] heapindexes;

    public Minheap(int size) {
        this.heap = new int[size + 1];
        this.heapsize = 0;

        this.heapindexes = new int[size];
    }

    /**
     * vanhemman indeksi keko taulukossa
     *
     * @param i
     * @return
     */
    private int parent(int i) {
        return i / 2;
    }

    /**
     * vasemman lapsen indeksi keko taulukossa
     *
     * @param i
     * @return
     */
    private int leftChild(int i) {
        return 2 * i;
    }

    /**
     * oikean lapsen indeksi keko taulukossa
     *
     * @param i
     * @return
     */
    private int rightChild(int i) {
        return 2 * i + 1;
    }

    /**
     * antaa arvion jonka mukaan määritellään kunkin keon alkion arvo.
     *
     * @param i
     * @return
     */
    public abstract double estimate(int i);

    /**
     * Palauttaa kekoehdon voimaan, jos kekoon (tai keossa oleviin alkioihin) on
     * tehty muutoksia. Aloittaa keon muokkaamisen tietystä indeksistä.
     *
     * @param i
     */
    private void heapify(int i) {
        int v = this.leftChild(i);
        int o = this.rightChild(i);
        int smaller;

        if (o <= this.heapsize) {
            if (this.estimate(this.heap[v]) < estimate(this.heap[o])) {
                smaller = v;
            } else {
                smaller = o;
            }

            if (estimate(this.heap[i]) > estimate(this.heap[smaller])) {
                this.swap(i, smaller);
                this.heapify(smaller);
            }
        } else if (v == this.heapsize && estimate(this.heap[i]) > estimate(this.heap[v])) {
            this.swap(i, v);
        }
    }

    /**
     * Palauttaa kekoehdon voimaan, kun alkioon on tehty muutoksia. Aloittaa
     * muokkaamisen tietystä alkiosta
     *
     * @param number
     */
    public void update(int number) {
        int i = this.heapindexes[number];
        while (i > 1 && estimate(this.heap[this.parent(i)]) > estimate(this.heap[i])) {
            this.swap(i, this.parent(i));
            i = this.parent(i);
        }
    }

    /**
     * vaihtaa kahden alkion paikan keossa.
     *
     * @param a
     * @param b
     */
    private void swap(int a, int b) {
        int tmp = this.heap[a];
        this.heap[a] = this.heap[b];
        this.heapindexes[this.heap[a]] = a;
        this.heap[b] = tmp;
        this.heapindexes[this.heap[b]] = b;
    }

    /**
     * lisää kekoon uuden alkion, säilyttäen kekoehdon voimassa.
     *
     * @param number
     */
    public void add(int number) {
        this.heapsize++;
        int i = this.heapsize;
        while (i > 1 && estimate(this.heap[this.parent(i)]) > estimate(number)) {
            this.heap[i] = heap[this.parent(i)];
            this.heapindexes[this.heap[i]] = i;
            i = this.parent(i);
        }
        this.heap[i] = number;
        this.heapindexes[number] = i;

    }

    /**
     * kertoo onko keko tyhjä
     *
     * @return
     */
    public boolean empty() {
        return this.heapsize == 0;
    }

    /**
     * ottaa keon ensimmäisen alkion ja palauttaa kekoehdon voimaan heapifyn
     * avulla.
     *
     * @return
     */
    public int takeSmallest() {

        int first = this.heap[1];
        this.heap[1] = this.heap[this.heapsize];
        this.heapindexes[this.heap[1]] = 1;

        this.heapsize--;
        this.heapify(1);
        return first;
    }

}
