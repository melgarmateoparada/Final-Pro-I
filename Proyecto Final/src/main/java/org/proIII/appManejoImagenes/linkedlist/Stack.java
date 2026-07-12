package org.proIII.appManejoImagenes.linkedlist;


public class Stack<A> {
    protected Node<A> raiz;

    public Stack(){}

    public void push(A dato) {
        Node<A> newNode = new Node<>(dato);
        newNode.setNext(raiz);
        raiz = newNode;
    }

    public boolean isEmpty() {
    return raiz ==null;
    }

    public A pop() {
        if(raiz == null){
            return null;
        }

        A value = raiz.getValue();
        raiz = raiz.getNext();
        return value;
    }
}
