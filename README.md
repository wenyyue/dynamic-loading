# dynamic-loading

## What is this
This project is a very simple example demonstrating how to load a class dynamically on a running application.

## Background
We often have some requirement that need to execute some tasks like brushing data in database using complex logic, 
which need you write code scripts, and then deploying it on your server to execute. Is there any way that does not need 
redeploy application after completing logic on local? Here is an example.

## Fundamental
Construct a customised ClassLoader receiving compiled class file from local, then load it and execute the method you specified.

## Example
You can look up it in `com.dynamicloading.controller.ExampleController.dynamicLoading`

## Notice
There is an important trap I encountered when I was doing this example, that is do not override the method `findClass` when implement 
the customised ClassLoader, we know that Java use mechanism **Parents Delegation** loading classes, a classloader only load
a class when it parental classloader cannot load the class, consequently, a problem is coming.
### Assumption: 
1. We do not specify the classloader's parent classloader
2. We override `findclass`

When we use our customised classloader to load a class, and somehow we need to load a new class the logic use, the classloader 
will load the new class using Parents Delegation mechanism, and if we do not specify parent classloader for the customised, 
AppClassLoader will be the default one, which has no capability to load in some situation such as the new class must be loaded by 
other customised ClassLoader like Spring..ClassLoader, then the customised classloader will load it using `findclass` 
and undoubtedly, that will be failed, cause our customised ClassLoader is specialised for our compiled class, which means
it is not able to load other classes.

## Conclusion
When define a customised ClassLoader, do not implement `findclass` but using `defineClass`, which is used to 
"_convert an array of bytes into an instance of class Class_", to load class directly, and for all the other loading behaviors
just let them be in default.