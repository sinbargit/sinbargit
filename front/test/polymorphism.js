class A{
    constructor()
    {
        this.polymorphism();
    }
    polymorphism(){
        console.log("A")
    }
}
class B extends A{
    constructor(param)
    {
        super();
    }
    polymorphism()
    {
        console.log("B")
        /**use param
         * business code**/
    }
}
new B()
