import { useState } from "react"
import PaymentForm from "./components/PaymentForm"
import "./App.css"

function App() {
    const [counter, setCounter] = useState(0)

    return (
        <div className="min-h-screen bg-gray-50">
            {/* Hero Section */}
            <section className="bg-gradient-to-r from-green-600 to-green-500 text-white py-16">
                <div className="max-w-7xl mx-auto px-4 text-center">
                    <div className="flex justify-center items-center gap-4 mb-6">
                        {/* Vite Logo */}
                        <svg
                            className="w-12 h-12"
                            viewBox="0 0 410 404"
                            fill="none"
                        >
                            <path
                                d="M399.641 59.5246L215.643 388.545C211.844 395.338 202.084 395.378 198.228 388.618L10.5817 59.5563C6.38087 52.1896 12.6802 43.2665 21.0281 44.7586L205.223 77.6824C206.398 77.8924 207.601 77.8904 208.776 77.6763L389.119 44.8058C397.439 43.2894 403.768 52.1434 399.641 59.5246Z"
                                fill="url(#paint0_linear)"
                            />
                            <path
                                d="M292.965 1.5744L156.801 28.2552C154.563 28.6937 152.906 30.5903 152.771 32.8664L144.395 174.33C144.198 177.662 147.258 180.248 150.51 179.498L188.42 170.749C191.967 169.931 195.172 173.055 194.443 176.622L183.18 231.775C182.422 235.487 185.907 238.661 189.532 237.56L212.947 230.446C216.577 229.344 220.065 232.527 219.297 236.242L201.398 322.875C200.278 328.294 207.486 331.249 210.492 326.603L212.5 323.5L323.454 102.072C325.312 98.3645 322.108 94.137 318.036 94.9228L279.014 102.454C275.347 103.161 272.227 99.746 273.262 96.1583L298.731 7.86689C299.767 4.27314 296.636 0.855181 292.965 1.5744Z"
                                fill="url(#paint1_linear)"
                            />
                            <defs>
                                <linearGradient
                                    id="paint0_linear"
                                    x1="6.00017"
                                    y1="32.9999"
                                    x2="235"
                                    y2="344"
                                    gradientUnits="userSpaceOnUse"
                                >
                                    <stop stopColor="#41D1FF" />
                                    <stop offset="1" stopColor="#BD34FE" />
                                </linearGradient>
                                <linearGradient
                                    id="paint1_linear"
                                    x1="194.651"
                                    y1="8.81818"
                                    x2="236.076"
                                    y2="292.989"
                                    gradientUnits="userSpaceOnUse"
                                >
                                    <stop stopColor="#FFEA83" />
                                    <stop
                                        offset="0.0833333"
                                        stopColor="#FFDD35"
                                    />
                                    <stop offset="1" stopColor="#FFA800" />
                                </linearGradient>
                            </defs>
                        </svg>
                        {/* React Logo */}
                        <svg
                            className="w-12 h-12"
                            viewBox="-11.5 -10.23174 23 20.46348"
                        >
                            <circle
                                cx="0"
                                cy="0"
                                r="2.05"
                                fill="currentColor"
                            />
                            <g
                                stroke="currentColor"
                                strokeWidth="1"
                                fill="none"
                            >
                                <ellipse rx="11" ry="4.2" />
                                <ellipse
                                    rx="11"
                                    ry="4.2"
                                    transform="rotate(60)"
                                />
                                <ellipse
                                    rx="11"
                                    ry="4.2"
                                    transform="rotate(120)"
                                />
                            </g>
                        </svg>
                    </div>
                    <h1 className="text-5xl font-bold mb-4">PayFlow Demo</h1>
                    <p className="text-lg text-green-50 max-w-2xl mx-auto">
                        Experience seamless payment processing with modern React
                        components and beautiful UI design
                    </p>
                </div>
            </section>

            {/* Modern Payment Solutions */}
            <section className="py-16 bg-gray-100">
                <div className="max-w-7xl mx-auto px-4">
                    <h2 className="text-4xl font-bold text-center text-gray-900 mb-4">
                        Modern Payment Solutions
                    </h2>
                    <p className="text-center text-gray-600 mb-12 max-w-3xl mx-auto">
                        Built with React and Vite for lightning-fast
                        performance. Secure, responsive, and user-friendly
                        payment forms that adapt to any device.
                    </p>

                    <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                        {/* Secure Processing Card */}
                        <div className="bg-white p-8 rounded-xl shadow-sm hover:shadow-md transition-shadow">
                            <div className="w-14 h-14 bg-green-100 rounded-full flex items-center justify-center mb-4 mx-auto">
                                <svg
                                    className="w-7 h-7 text-green-600"
                                    fill="none"
                                    stroke="currentColor"
                                    viewBox="0 0 24 24"
                                >
                                    <path
                                        strokeLinecap="round"
                                        strokeLinejoin="round"
                                        strokeWidth={2}
                                        d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z"
                                    />
                                </svg>
                            </div>
                            <h3 className="text-xl font-semibold text-center mb-3">
                                Secure Processing
                            </h3>
                            <p className="text-gray-600 text-center text-sm">
                                End-to-end encryption ensures your payment data
                                is always protected and secure.
                            </p>
                        </div>

                        {/* Lightning Fast Card */}
                        <div className="bg-white p-8 rounded-xl shadow-sm hover:shadow-md transition-shadow">
                            <div className="w-14 h-14 bg-green-100 rounded-full flex items-center justify-center mb-4 mx-auto">
                                <svg
                                    className="w-7 h-7 text-green-600"
                                    fill="none"
                                    stroke="currentColor"
                                    viewBox="0 0 24 24"
                                >
                                    <path
                                        strokeLinecap="round"
                                        strokeLinejoin="round"
                                        strokeWidth={2}
                                        d="M13 10V3L4 14h7v7l9-11h-7z"
                                    />
                                </svg>
                            </div>
                            <h3 className="text-xl font-semibold text-center mb-3">
                                Lightning Fast
                            </h3>
                            <p className="text-gray-600 text-center text-sm">
                                Powered by Vite and React for instant load times
                                and smooth interactions.
                            </p>
                        </div>

                        {/* Mobile Ready Card */}
                        <div className="bg-white p-8 rounded-xl shadow-sm hover:shadow-md transition-shadow">
                            <div className="w-14 h-14 bg-green-100 rounded-full flex items-center justify-center mb-4 mx-auto">
                                <svg
                                    className="w-7 h-7 text-green-600"
                                    fill="none"
                                    stroke="currentColor"
                                    viewBox="0 0 24 24"
                                >
                                    <path
                                        strokeLinecap="round"
                                        strokeLinejoin="round"
                                        strokeWidth={2}
                                        d="M12 18h.01M8 21h8a2 2 0 002-2V5a2 2 0 00-2-2H8a2 2 0 00-2 2v14a2 2 0 002 2z"
                                    />
                                </svg>
                            </div>
                            <h3 className="text-xl font-semibold text-center mb-3">
                                Mobile Ready
                            </h3>
                            <p className="text-gray-600 text-center text-sm">
                                Fully responsive design that works perfectly on
                                all devices and screen sizes.
                            </p>
                        </div>
                    </div>
                </div>
            </section>

            {/* Interactive Demo */}
            <section className="py-16 bg-white">
                <div className="max-w-7xl mx-auto px-4">
                    <div className="max-w-2xl mx-auto bg-white p-8 rounded-xl shadow-sm border border-gray-200">
                        <h2 className="text-3xl font-bold text-center text-gray-900 mb-8">
                            Interactive Demo
                        </h2>
                        <div className="text-center">
                            <button
                                onClick={() => setCounter(counter + 1)}
                                className="bg-green-600 hover:bg-green-700 text-white font-semibold px-8 py-3 rounded-lg transition-colors shadow-md hover:shadow-lg"
                            >
                                ✓ Click Counter: {counter}
                            </button>
                            <p className="text-gray-600 text-sm mt-4">
                                Edit{" "}
                                <code className="bg-gray-100 px-2 py-1 rounded text-sm">
                                    src/App.jsx
                                </code>{" "}
                                and save to test HMR (Hot Module Replacement)
                            </p>
                        </div>
                    </div>
                </div>
            </section>

            {/* Payment Form Section */}
            <section className="py-16 bg-gray-900">
                <div className="max-w-7xl mx-auto px-4">
                    <div className="text-center mb-12">
                        <h2 className="text-4xl font-bold text-white mb-4">
                            Try Our Payment Form
                        </h2>
                        <p className="text-gray-300 max-w-2xl mx-auto">
                            Experience our beautifully designed payment form
                            with real-time validation, secure input handling,
                            and smooth animations.
                        </p>
                    </div>
                    <PaymentForm />
                </div>
            </section>

            {/* Footer */}
            <footer className="bg-gray-950 text-gray-400 py-12">
                <div className="max-w-7xl mx-auto px-4">
                    <div className="text-center mb-8">
                        <h3 className="text-white text-xl font-semibold mb-4">
                            PayFlow Demo
                        </h3>
                        <p className="text-sm mb-6">
                            A modern payment demo app built with React, Vite,
                            and Tailwind CSS. Showcasing best practices in UI/UX
                            and web development.
                        </p>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-3 gap-8 mb-8">
                        <div className="text-center">
                            <h4 className="text-white font-semibold mb-3">
                                Technologies
                            </h4>
                            <ul className="space-y-2 text-sm">
                                <li className="flex items-center justify-center gap-2">
                                    <span>⚛️</span> React 18
                                </li>
                                <li className="flex items-center justify-center gap-2">
                                    <span>⚡</span> Vite
                                </li>
                                <li className="flex items-center justify-center gap-2">
                                    <span>🎨</span> Tailwind CSS
                                </li>
                            </ul>
                        </div>

                        <div className="text-center">
                            <h4 className="text-white font-semibold mb-3">
                                Features
                            </h4>
                            <ul className="space-y-2 text-sm">
                                <li>✓ Secure & Encrypted</li>
                                <li>✓ Mobile Optimized</li>
                                <li>✓ Real-time Validation</li>
                            </ul>
                        </div>

                        <div className="text-center">
                            <h4 className="text-white font-semibold mb-3">
                                Learn More
                            </h4>
                            <ul className="space-y-2 text-sm">
                                <li>
                                    <a
                                        href="https://react.dev"
                                        target="_blank"
                                        rel="noopener noreferrer"
                                        className="hover:text-white transition-colors"
                                    >
                                        React Docs
                                    </a>
                                </li>
                                <li>
                                    <a
                                        href="https://vitejs.dev"
                                        target="_blank"
                                        rel="noopener noreferrer"
                                        className="hover:text-white transition-colors"
                                    >
                                        Vite Docs
                                    </a>
                                </li>
                                <li>
                                    <a
                                        href="https://github.com"
                                        target="_blank"
                                        rel="noopener noreferrer"
                                        className="hover:text-white transition-colors"
                                    >
                                        GitHub
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </div>

                    <div className="border-t border-gray-800 pt-8 text-center text-sm">
                        <p>
                            Built with <span className="text-red-500">❤️</span>{" "}
                            using modern web technologies
                        </p>
                    </div>
                </div>
            </footer>
        </div>
    )
}

export default App
