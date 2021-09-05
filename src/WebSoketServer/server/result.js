
class Result{
    constructor(tag,msg){
        this.tag=tag;
        this.msg=msg;
    }
}

const makeOk = (msg) =>
    new Result("Ok", msg);

    
const makeFailure = (msg) =>
    new Result("Failure", msg);

const isOk = (r) =>
     r.tag === "Ok";

const isFailure = (r) =>
    r.tag === "Failure";

const bind = (r, f) =>//(Result<U>, (x: T) => Result<U>)=> Result<U>
    isOk(r) ? f(r.value) : r;

const either = (r, ifOk, ifFailure) =>//<T, U>( Result<T>, ifOk: (value: T) => U, ifFailure: (message: string) => U): U
    isOk(r) ? ifOk(r.value) : ifFailure(r.message);



// Purpose: Like map on an array - but when the transformer function applied returns a Result<T>
//          With f: T=>Result<U> and list: T[] return a Result<U[]> 
//          If one of the items of the list fails on f - returns the Failure on the first item that fails.
// Example: 
// mapResult((x) => x === 0 ? makeFailure("div by 0") : makeOk(1/x), [1,2]) ==> {tag:"Ok", value:[1, 0.5]}
// mapResult((x) => x === 0 ? makeFailure("div by 0") : makeOk(1/x), [1,0,2]) ==> {tag:"Failure", message:"div by 0"}
const mapResult = (f, list) =>//f: T=>Result<U> and list: T[] return a Result<U[]> 
    isEmpty(list) ? makeOk([]) :
    bind(f(first(list)), 
         (fa) => bind(mapResult(f, rest(list)), 
                         (fas) => makeOk(fa.concat(fas))
                    )
                );
//<T1, T2, T3>(f: (x: T1, y: T2) => Result<T3>, xs: T1[], ys: T2[]): Result<T3[]> 
const zipWithResult = (f, xs, ys) =>
    xs.length === 0 || ys.length === 0 ? makeOk([]) :
    bind(f(first(xs), first(ys)),
         (fxy) => bind(zipWithResult(f, rest(xs), rest(ys)),
                           (fxys) => makeOk(cons(fxy, fxys))));
//<T1, T2, T3>(f: (x: T1, y: T2) => Result<T3>): (xr: Result<T1>, yr: Result<T2>) => Result<T3>
const safe2 = (f)=>
    (xr, yr) =>
        bind(xr, (x) => bind(yr, (y) => f(x, y)));

//<T1, T2, T3, T4>(f: (x: T1, y: T2, z: T3) => Result<T4>): (xr: Result<T1>, yr: Result<T2>, zr: Result<T3>) => Result<T4>
const safe3 = (f) =>
    (xr, yr, zr) =>
        bind(xr, (x) => bind(yr, (y) => bind(zr, (z) => f(x, y, z))));


module.exports={makeOk,makeFailure,isFailure,isOk,bind};
